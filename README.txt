【タスク管理システム - 成果物提出】
提出者：高梨直子

■ アプリケーション名
TaskManager

■ ログイン画面URL
http://localhost:8080/TaskManager/app/login

■ テストアカウント
ユーザー名：test_user
パスワード：password

■ 開発環境
・Java（Servlet / JSP）
・Apache Tomcat 9
・MySQL（XAMPP）
・Eclipse

■ データベース
データベース名：taskmanager
使用テーブル：users / tasks

■ 実装済み機能
・ログイン / ログアウト機能（認証）
・タスク一覧表示（自分のタスクのみ）
・タスク新規作成（Create）
・タスク編集（Update）
・タスク削除（Delete）
・検索機能（キーワード検索）
・並べ替え機能（新しい順 / 古い順）
・ページング機能
・お気に入り機能（★トグル・状態保持）

■ セキュリティ対策
・ログインユーザー以外のタスク操作を禁止（所有者チェック）
・不正なtaskId（未指定・文字列）に対する400エラー対応
・他人のtaskIdアクセス時の403制御


