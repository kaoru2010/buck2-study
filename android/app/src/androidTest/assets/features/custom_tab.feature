Feature: カスタムタブボタンの動作確認

  Scenario: ユーザーがドキュメントサイトを開く
    Given アプリが起動している
    When カスタムタブボタンをタップする
    Then URL "https://developer.android.com/" が開かれる
