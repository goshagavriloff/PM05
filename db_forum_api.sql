-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Хост: db
-- Время создания: Сен 30 2024 г., 16:17
-- Версия сервера: 11.2.2-MariaDB-1:11.2.2+maria~ubu2204
-- Версия PHP: 8.1.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `db_forum_api`
--

DELIMITER $$
--
-- Процедуры
--
CREATE DEFINER=`root`@`%` PROCEDURE `api_create_message` (IN `_login` CHAR(100), IN `_title` CHAR(100), IN `_topic` INT(11))   BEGIN
DECLARE _author INT DEFAULT NULL;
DECLARE _isTopicExists int DEFAULT null;
SET _author=(SELECT ID FROM `users` WHERE `name`=_login);
SET _isTopicExists=(SELECT ID FROM `topics` WHERE ID=_topic);

IF ((_author IS NOT NULL) and (_isTopicExists IS NOT NULL)) THEN
INSERT INTO `messages`(`title`, `author`, `date_create`, `topic`) VALUES (_title,_author,now(),_topic);
select * from `messages` where 
(title=_title) and (topic=_topic) and (author=_author);

END IF;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `api_create_topic` (IN `_login` CHAR(100), IN `_title` CHAR(100), IN `_descr` CHAR(100))   BEGIN
DECLARE _author INT DEFAULT NULL;
SET _author=(SELECT ID FROM `users` WHERE `name`=_login);

IF (_author IS NOT NULL) THEN
INSERT INTO `topics`( `title`, `author`, `date_create`, `descr`) VALUES (_title,_author,now(),_descr);

select * from `topics` where 
(title=_title) and (descr=_descr) and (author=_author);

END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `api_register` (IN `_login` CHAR(100), IN `_pwd` CHAR(100))   BEGIN
DECLARE RES  INT;
SET RES=(SELECT COUNT(*) FROM `users` WHERE `name`=_login AND `password`=_pwd);

IF (RES=0)
THEN
 INSERT IGNORE INTO `users` SET `name`=_login, `role`=2, `password`=_pwd;
 SELECT * FROM users  WHERE `name`=_login AND `password`=_pwd;

END IF;

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Структура таблицы `messages`
--

CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `title` char(100) NOT NULL,
  `author` int(11) NOT NULL,
  `date_create` datetime NOT NULL,
  `topic` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `name` char(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

--
-- Дамп данных таблицы `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'admin'),
(2, 'user'),
(3, 'moderator');

-- --------------------------------------------------------

--
-- Структура таблицы `topics`
--

CREATE TABLE `topics` (
  `id` int(11) NOT NULL,
  `title` char(100) NOT NULL,
  `author` int(11) NOT NULL,
  `date_create` datetime NOT NULL,
  `descr` char(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `topics`
--

INSERT INTO `topics` (`id`, `title`, `author`, `date_create`, `descr`) VALUES
(1, 'Кто взял в столовой мой пирожок?', 1, '2024-09-24 16:24:18', ''),
(3, 'asd', 1, '2024-09-30 15:50:41', 'a');

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` char(100) NOT NULL,
  `role` int(11) NOT NULL,
  `password` char(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`id`, `name`, `role`, `password`) VALUES
(1, 'john@mail.com', 1, 'changeme'),
(2, 'r@r.ru', 3, '123456'),
(3, 'b@b.ru', 2, 'bbb'),
(11, 'reg', 2, '123'),
(12, 'bf', 2, '123'),
(13, 'as', 2, '12'),
(17, 'asdfg', 2, '123');

-- --------------------------------------------------------

--
-- Дублирующая структура для представления `view_messages`
-- (См. Ниже фактическое представление)
--
CREATE TABLE `view_messages` (
`id` int(11)
,`title` char(100)
,`date_create` datetime
,`topic` int(11)
,`author` char(100)
);

-- --------------------------------------------------------

--
-- Дублирующая структура для представления `view_topics`
-- (См. Ниже фактическое представление)
--
CREATE TABLE `view_topics` (
`id` int(11)
,`date_create` varchar(19)
,`title` char(100)
,`login` char(100)
);

-- --------------------------------------------------------

--
-- Структура для представления `view_messages`
--
DROP TABLE IF EXISTS `view_messages`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `view_messages`  AS SELECT `messages`.`id` AS `id`, `messages`.`title` AS `title`, `messages`.`date_create` AS `date_create`, `messages`.`topic` AS `topic`, `users`.`name` AS `author` FROM ((`messages` join `topics` on(`messages`.`topic` = `topics`.`id`)) join `users` on(`messages`.`author` = `users`.`id`)) ;

-- --------------------------------------------------------

--
-- Структура для представления `view_topics`
--
DROP TABLE IF EXISTS `view_topics`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `view_topics`  AS SELECT `topics`.`id` AS `id`, date_format(`topics`.`date_create`,'%d.%m.%y %H:%i') AS `date_create`, `topics`.`title` AS `title`, `users`.`name` AS `login` FROM (`topics` join `users` on(`topics`.`author` = `users`.`id`)) ;

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `topic` (`topic`),
  ADD KEY `author` (`author`);

--
-- Индексы таблицы `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `topics`
--
ALTER TABLE `topics`
  ADD PRIMARY KEY (`id`),
  ADD KEY `author` (`author`);

--
-- Индексы таблицы `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `role` (`role`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT для таблицы `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT для таблицы `topics`
--
ALTER TABLE `topics`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`topic`) REFERENCES `topics` (`id`),
  ADD CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`author`) REFERENCES `users` (`id`);

--
-- Ограничения внешнего ключа таблицы `topics`
--
ALTER TABLE `topics`
  ADD CONSTRAINT `topics_ibfk_1` FOREIGN KEY (`author`) REFERENCES `users` (`id`);

--
-- Ограничения внешнего ключа таблицы `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
