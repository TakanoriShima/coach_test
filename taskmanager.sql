-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- ホスト: 127.0.0.1
-- 生成日時: 2026-02-20 05:21:08
-- サーバのバージョン： 10.4.32-MariaDB
-- PHP のバージョン: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- データベース: `taskmanager`
--

-- --------------------------------------------------------

--
-- テーブルの構造 `tasks`
--

CREATE TABLE `tasks` (
  `task_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `status` varchar(50) NOT NULL DEFAULT 'pending',
  `priority` varchar(50) NOT NULL DEFAULT 'medium',
  `is_favorite` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- テーブルのデータのダンプ `tasks`
--

INSERT INTO `tasks` (`task_id`, `user_id`, `title`, `description`, `status`, `priority`, `is_favorite`, `created_at`, `updated_at`) VALUES
(1, 1, 'プレゼン資料作成', '成果物提出用の資料を作る', 'in_progress', 'high', 1, '2026-02-19 13:58:48', '2026-02-19 13:58:48'),
(2, 1, '買い物リスト作成', '日用品をメモする', 'pending', 'medium', 0, '2026-02-19 13:58:48', '2026-02-20 00:42:23'),
(3, 1, 'Java学習', '課題の復習', 'completed', 'medium', 1, '2026-02-19 13:58:48', '2026-02-19 13:58:48'),
(4, 1, '歯医者予約', '電話で予約する', 'pending', 'low', 0, '2026-02-19 13:58:48', '2026-02-19 13:58:48'),
(5, 1, 'レポート作成', '提出用レポート', 'in_progress', 'high', 0, '2026-02-19 13:58:48', '2026-02-19 14:29:37'),
(6, 1, '美容室予約', 'いつものところ', 'pending', 'medium', 0, '2026-02-19 14:27:23', '2026-02-19 14:27:23'),
(7, 1, '編集テスト用（更新済み）', '編集テスト', 'pending', 'medium', 0, '2026-02-20 00:43:38', '2026-02-20 01:36:56'),
(9, 1, '宅配便発送', '発送', 'pending', 'medium', 1, '2026-02-20 00:49:43', '2026-02-20 01:38:54'),
(10, 1, '避難用品のチェック', 'これ大事', 'pending', 'medium', 0, '2026-02-20 00:52:09', '2026-02-20 01:39:25'),
(11, 1, '商品写真撮影', NULL, 'pending', 'low', 0, '2026-02-20 00:54:20', '2026-02-20 00:54:20'),
(12, 1, '新しいスニーカーの候補を探す', NULL, 'pending', 'low', 0, '2026-02-20 00:55:15', '2026-02-20 01:39:09'),
(20, 1, '動画撮影タスク', '成果物提出用の動画を撮影する', 'in_progress', 'high', 0, '2026-02-20 01:36:24', '2026-02-20 01:36:24');

-- --------------------------------------------------------

--
-- テーブルの構造 `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- テーブルのデータのダンプ `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `email`, `created_at`, `updated_at`) VALUES
(1, 'test_user', 'password', NULL, '2026-02-19 14:08:50', '2026-02-19 14:08:50');

--
-- ダンプしたテーブルのインデックス
--

--
-- テーブルのインデックス `tasks`
--
ALTER TABLE `tasks`
  ADD PRIMARY KEY (`task_id`),
  ADD KEY `idx_tasks_user_fav` (`user_id`,`is_favorite`);

--
-- テーブルのインデックス `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- ダンプしたテーブルの AUTO_INCREMENT
--

--
-- テーブルの AUTO_INCREMENT `tasks`
--
ALTER TABLE `tasks`
  MODIFY `task_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- テーブルの AUTO_INCREMENT `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- ダンプしたテーブルの制約
--

--
-- テーブルの制約 `tasks`
--
ALTER TABLE `tasks`
  ADD CONSTRAINT `tasks_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
