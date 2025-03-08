#!/bin/bash
# ============================================
# Gitブランチクリーンアップスクリプト (Mac/Linux対応版)
#
# このスクリプトは、現在のブランチが main ブランチ（または適宜 master 等）であることを前提とし、
# マージ済みのローカルブランチおよびリモートブランチを削除します。
#
# 注意:
# ・削除対象となるブランチは、main ブランチ以外のもののみです。
# ・作業前に必ず変更内容がコミット済みであるか、バックアップを取ってから実行してください。
# ============================================

# 厳格なエラーチェック
set -euo pipefail
IFS=$'\n\t'

# 設定
MAIN_BRANCH="main"  # 必要に応じて "master" 等に変更
REMOTE="origin"

# 現在のブランチを取得
CURRENT_BRANCH=$(git symbolic-ref --short HEAD)

# 現在のブランチが MAIN_BRANCH でなければ処理を中断
if [ "$CURRENT_BRANCH" != "$MAIN_BRANCH" ]; then
    echo "現在、${CURRENT_BRANCH} ブランチにいます。削除処理を行うには、まず ${MAIN_BRANCH} ブランチにチェックアウトしてください。"
    exit 1
fi

echo "=== マージ済みのローカルブランチ削除処理開始 ==="

# マージ済みのローカルブランチ一覧を取得し、MAIN_BRANCH以外のブランチを削除
for branch in $(git branch --merged | sed 's/\*//'); do
    branch=$(echo "$branch" | xargs)  # 前後の空白を除去
    if [ -n "$branch" ] && [ "$branch" != "$MAIN_BRANCH" ]; then
        echo "削除: ${branch}"
        git branch -d "$branch"
    fi
done

echo "=== マージ済みのローカルブランチ削除完了 ==="
echo ""

echo "=== マージ済みのリモートブランチ削除処理開始 ==="

# リモートのMAIN_BRANCHが最新であることをフェッチ
git fetch "$REMOTE"

# リモートブランチ一覧を取得し、MAIN_BRANCH以外で、MAIN_BRANCHへマージ済みのブランチを削除
for branch in $(git branch -r --merged "${REMOTE}/${MAIN_BRANCH}" | sed "s|${REMOTE}/||"); do
    branch=$(echo "$branch" | xargs)
    if [ -n "$branch" ] && [ "$branch" != "$MAIN_BRANCH" ]; then
        echo "リモート削除: ${branch}"
        git push "$REMOTE" --delete "$branch"
    fi
done

echo "=== マージ済みのリモートブランチ削除完了 ==="
echo "ブランチのクリーンアップが完了しました。"

exit 0
