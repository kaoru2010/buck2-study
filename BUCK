# A list of available rules and their signatures can be found here: https://buck2.build/docs/prelude/globals/

genrule(
    name = "hello_world",
    out = "out.txt",
    cmd = "echo BUILT BY BUCK2> $OUT",
)

sh_binary(
    name = 'git_clean_merged_branches',
    main = 'git/git-clean-merged-branches.sh',
)

sh_test(
    name = 'bats_sample',
    test = 'buck2_study_toolchains//:bats_bin',
    args = [
        'bats/sample.bats',
    ],
)

test_suite(
  name = "all_tests",
  tests = [
    ':bats_sample',
    '//spin:all_tests',
    ':pict_tests',
  ],
)

test_suite(
  name = 'pict_tests',
  tests = [
    ':pict_test',
    ':pict_cucumber_test',
    ':pict_negative_testing_test',
  ],
)

sh_test(
    name = 'pict_test',
    test = 'buck2_study_toolchains//:pict_bin',
    args = [
        'pict/PICT_OS_Browser_InputConstraintDemo.pict',
        '/s',
    ],
)

sh_test(
    name = 'pict_cucumber_test',
    test = ':pict_cucumber',
)

sh_test(
    name = 'pict_negative_testing_test',
    test = ':pict_negative_testing',
)

load("@buck2_study_toolchains//:pict_cucumber_table.bzl", "pict_cucumber_table")
pict_cucumber_table(
    name = "pict_cucumber",
    src = "pict/PICT_OS_Browser_InputConstraintDemo.pict",
)

pict_cucumber_table(
    name = "pict_negative_testing",
    src = "pict/negative_testing.pict",
)
