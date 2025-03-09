load("@prelude//:paths.bzl", "paths")

def _android_rule_impl(ctx: AnalysisContext) -> list[Provider]:
    out_dir = ctx.actions.declare_output(ctx.attrs.out, dir = True)
    build_log = ctx.actions.declare_output('build_log.log')

    root_project_dir = paths.dirname(ctx.attrs.settings_gradle.short_path)

    if type(ctx.attrs.srcs) == type([]):
        # FIXME: We should always use the short_path, but currently that is sometimes blank.
        # See fbcode//buck2/tests/targets/rules/genrule:genrule-dot-input for a test that exposes it.
        symlinks = {src.short_path: src for src in ctx.attrs.srcs}

        if len(symlinks) != len(ctx.attrs.srcs):
            for src in ctx.attrs.srcs:
                name = src.short_path
                if symlinks[name] != src:
                    msg = "genrule srcs include duplicative name: `{}`. ".format(name)
                    msg += "`{}` conflicts with `{}`".format(symlinks[name].owner, src.owner)
                    fail(msg)
    else:
        symlinks = ctx.attrs.srcs
    srcs_artifact = ctx.actions.copied_dir("srcs", symlinks)

    patches = [
        cmd_args([ctx.attrs.patch_command, '<', patch_file], delimiter=" ", relative_to = srcs_artifact)
        for patch_file in ctx.attrs.patch_files
    ]

    sh = ctx.actions.write(
        "build.sh",
        cmd_args([
            "#!/bin/sh",
            "set -e",
            "set -o pipefail",
            "",
            cmd_args(["cd", srcs_artifact], delimiter=" "),
            "export BUCK2_WORKSPACE=`pwd`",
            "",
            patches,
            "",
            cmd_args(["cd", root_project_dir], delimiter=" "),
            cmd_args(["./gradlew", ctx.attrs.args, '|', 'tee', build_log.as_output()], delimiter=" ", relative_to = srcs_artifact.project(root_project_dir)),
            "",
            'cd "$BUCK2_WORKSPACE"',
            cmd_args("export TMP=${TMPDIR:-/tmp}"),
            cmd_args(srcs_artifact.project(root_project_dir), format = "export ROOT_PROJECT_DIR={}", relative_to = srcs_artifact),
            cmd_args(out_dir.as_output(), format="export OUT={}", relative_to = srcs_artifact),
            cmd_args([ctx.attrs.cmd]),
        ]),
        is_executable = True,
    )
    ctx.actions.run(
        cmd_args(
            ["sh", sh],
            hidden = [
                out_dir.as_output(),
                build_log.as_output(),
                srcs_artifact,
                ctx.attrs.settings_gradle,
                ctx.attrs.args,
                ctx.attrs.cmd,
                ctx.attrs.out,
                ctx.attrs.patch_files,
                ctx.attrs.patch_command,
            ],
        ),
        category = 'android',
        always_print_stderr = True,
    )

    sub_targets = ctx.attrs.sub_targets
    if type(sub_targets) == type([]):
        sub_targets = {
            path: [DefaultInfo(default_output = out_dir.project(path))]
            for path in sub_targets
        }
    elif type(sub_targets) == type({}):
        sub_targets = {
            name: [DefaultInfo(default_outputs = [out_dir.project(path) for path in paths])]
            for name, paths in sub_targets.items()
        }
    else:
        fail("sub_targets must be a list or dict")

    sub_targets['build_log.log'] = [DefaultInfo(default_output = build_log)];

    return [
        DefaultInfo(
            default_output = out_dir,
            sub_targets = sub_targets,
            other_outputs = [
                srcs_artifact,
                build_log,
            ],
        ),
        #ExternalRunnerTestInfo(
        #    type = "spin_test",
        #    command = [cmd_args(["sh", test_sh], hidden = [build_dir, ctx.attrs.spin_bin[RunInfo], ctx.attrs.src])],
        #),
    ]

android_rule = rule(
    impl = _android_rule_impl,
    attrs = {
        "srcs": attrs.list(attrs.source(), default = []),
        "settings_gradle": attrs.source(),
        "patch_files": attrs.list(attrs.source(), default = []),
        "patch_command": attrs.option(attrs.string(), default = 'patch -p1'),
        "args": attrs.list(attrs.string(), default = ['tasks']),
        "out": attrs.string(),
        "cmd": attrs.string(),
        "sub_targets": attrs.one_of(
            attrs.list(attrs.string()),
            attrs.dict(
                attrs.string(),
                attrs.list(attrs.string()),
            ),
            default = [],
        ),
    }
)
