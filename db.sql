/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : db

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 26/02/2025 23:18:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for food
-- ----------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `fd_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `intro` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` int(11) NULL DEFAULT NULL,
  `photo` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fd_status` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of food
-- ----------------------------
INSERT INTO `food` VALUES (1, '宫保鸡丁', '鸡肉丁炒花生米、辣椒和葱姜，口味香辣微甜，是川菜经典菜品之一。', 100, '/imgs/3c61b0f6-83a6-4263-9549-ad0981c817e0.jpg', 1);
INSERT INTO `food` VALUES (2, '2北京烤鸭', '鸭肉皮脆肉嫩，搭配薄饼、葱丝和甜面酱，是北京地区的传统名菜。', 120, '/imgs/9ac9c92f-f783-4425-8c27-ef8c72e9478a.jpg', 0);
INSERT INTO `food` VALUES (3, '3鱼香肉丝', '猪肉丝炒配以香辣酱汁、木耳和胡萝卜，味道鲜美多汁，为川菜代表之一。', 90, '/imgs/3.jpg', 0);
INSERT INTO `food` VALUES (4, '4水煮鱼', '鲜嫩鱼片、豆芽、辣椒在麻辣汤中煮制，火辣鲜香，是川菜中的招牌菜。', 80, '/imgs/4.jpg', 0);
INSERT INTO `food` VALUES (5, '5回锅肉', '猪肉片与辣椒豆瓣酱炒制，色泽红亮，口感酥脆香辣，是川菜的经典菜品之一。', 110, '/imgs/5.jpg', 0);
INSERT INTO `food` VALUES (6, '6麻婆豆腐', '豆腐与肉末、豆瓣酱一起炒制，麻辣鲜香，为川菜中的经典菜品。', 95, '/imgs/6.jpg', 1);
INSERT INTO `food` VALUES (7, '7葱爆羊肉', '羊肉片炒配葱段，香气扑鼻，肉质鲜嫩，是北方菜肴之一。', 130, 'http://localhost:8080/Ordering-System/imgs/QQ20240101130329.png', 1);
INSERT INTO `food` VALUES (8, '8糖醋排骨', '排骨裹面糊炸后淋上糖醋汁，酸甜口感，是江苏菜的特色菜品。', 85, 'http://localhost:8080/Ordering-System/imgs/QQ20240101130329.png', 0);
INSERT INTO `food` VALUES (9, '9东坡肉', '五花肉先煮后炖，色泽红亮、肥而不腻，是浙菜的代表菜。', 115, 'http://localhost:8080/Ordering-System/imgs/QQ20240101130329.png', 0);
INSERT INTO `food` VALUES (10, '10麻辣香锅', '各种肉类、蔬菜和豆制品与辣椒一起炒制，香辣诱人，为川菜特色之一。', 105, '/imgs/10.jpg', 1);
INSERT INTO `food` VALUES (11, '11红烧肉', '肥瘦相间的猪肉块，炖制入味，色泽鲜红，是中国传统名菜之一。', 100, '/imgs/11.jpg', 1);
INSERT INTO `food` VALUES (12, '12小笼包', '鲜肉馅包裹在薄皮内蒸制，汤汁丰富，是上海著名的点心之一。', 120, '/imgs/12.jpg', 1);
INSERT INTO `food` VALUES (13, '13松鼠鱼', '鱼肉滑嫩，裹上酸甜的番茄酱，再撒上松仁，是江苏菜的招牌菜。', 90, '/imgs/13.jpg', 1);
INSERT INTO `food` VALUES (14, '14酸辣汤', '酸中带辣、酸中带鲜，汤中有肉丝、木耳和豆腐等食材，是川菜特色之一。', 80, '/imgs/14.jpg', 1);
INSERT INTO `food` VALUES (15, '15蚝油牛肉', '牛肉片炒配蚝油、青椒等，口感嫩滑，是粤菜的经典菜品之一。', 110, '/imgs/15.jpg', 1);
INSERT INTO `food` VALUES (16, '16扬州炒饭', '米饭与蔬菜、肉类等配料翻炒，口感香糯，是江苏菜的特色之一。', 95, '/imgs/16.jpg', 1);
INSERT INTO `food` VALUES (17, '17干煸四季豆', '四季豆炒配辣椒和蒜末，香辣爽脆，是川菜中的一道下饭菜。', 130, '/imgs/17.jpg', 1);
INSERT INTO `food` VALUES (18, '18锅包肉', '猪肉片裹面粉炸制，淋上酸甜的酱汁，香脆可口，是东北菜的招牌菜。', 85, '/imgs/18.jpg', 1);
INSERT INTO `food` VALUES (19, '19冰糖葫芦', '水果或果蔬串在竹签上，裹上糖浆，外脆内软，是传统的甜点。', 115, '/imgs/19.jpg', 1);
INSERT INTO `food` VALUES (20, '20蒜蓉西兰花', '西兰花炒配蒜蓉，口感清爽，是粤菜中的一道常见菜品。', 105, '/imgs/20.jpg', 1);
INSERT INTO `food` VALUES (22, '狗不理包子', ' 狗不理包子', 99, 'http://localhost:8080/Delivery-system/imgs/QQ20240101130329.png', 0);
INSERT INTO `food` VALUES (23, '入坤', '大山散养入坤', 100, '/imgs/91942707-f7f1-45b6-ad55-88fab266770e.jpg', 0);

-- ----------------------------
-- Table structure for foodlist
-- ----------------------------
DROP TABLE IF EXISTS `foodlist`;
CREATE TABLE `foodlist`  (
  `fd_id` int(11) NOT NULL COMMENT '食品ID',
  `or_id` int(11) NOT NULL COMMENT '订单ID',
  `fd_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '食品名称',
  `price` int(11) NOT NULL COMMENT '单价',
  `count` int(11) NOT NULL COMMENT '数量',
  `sum` int(11) NOT NULL COMMENT '小计',
  PRIMARY KEY (`fd_id`, `or_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of foodlist
-- ----------------------------
INSERT INTO `foodlist` VALUES (1, 61, '宫保鸡丁', 100, 2, 200);
INSERT INTO `foodlist` VALUES (2, 55, '2北京烤鸭', 12000, 3, 36000);
INSERT INTO `foodlist` VALUES (2, 56, '2北京烤鸭', 12000, 2, 24000);
INSERT INTO `foodlist` VALUES (2, 57, '2北京烤鸭', 12000, 3, 36000);
INSERT INTO `foodlist` VALUES (2, 58, '2北京烤鸭', 120, 3, 360);
INSERT INTO `foodlist` VALUES (3, 55, '3鱼香肉丝', 9000, 1, 9000);
INSERT INTO `foodlist` VALUES (3, 56, '3鱼香肉丝', 9000, 3, 27000);
INSERT INTO `foodlist` VALUES (4, 56, '4水煮鱼', 8000, 2, 16000);
INSERT INTO `foodlist` VALUES (4, 60, '4水煮鱼', 80, 6, 480);
INSERT INTO `foodlist` VALUES (6, 59, '6麻婆豆腐', 95, 1, 95);
INSERT INTO `foodlist` VALUES (6, 61, '6麻婆豆腐', 95, 1, 95);
INSERT INTO `foodlist` VALUES (19, 60, '19冰糖葫芦', 115, 1, 115);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `us_id` int(11) NULL DEFAULT NULL,
  `sum` int(11) NULL DEFAULT NULL,
  `star` int(11) NULL DEFAULT -1,
  `or_status` int(11) NULL DEFAULT -1,
  `table_number` int(11) NULL DEFAULT NULL COMMENT '桌号（堂食时使用）',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系电话',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '送餐地址（外卖时使用）',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `order_type` int(11) NULL DEFAULT 1 COMMENT '订单类型(1:堂食,2:外卖)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (58, '2025-02-26 19:17:18', 57, 360, -1, 1, 5, '13319381001', NULL, '', 1);
INSERT INTO `orders` VALUES (59, '2025-02-26 19:17:33', 57, 95, 5, 1, 2, '13319381001', NULL, '', 1);
INSERT INTO `orders` VALUES (60, '2025-02-26 19:32:40', 57, 1095, 4, 1, NULL, '13319381001', '江西理工大学', '', 2);
INSERT INTO `orders` VALUES (61, '2025-02-26 22:03:27', 57, 295, -1, 1, 2, '13319381001', NULL, '', 1);

-- ----------------------------
-- Table structure for settings
-- ----------------------------
DROP TABLE IF EXISTS `settings`;
CREATE TABLE `settings`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `setting_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设置项键名',
  `setting_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设置项值',
  `setting_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设置项描述',
  `created_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `setting_key`(`setting_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of settings
-- ----------------------------
INSERT INTO `settings` VALUES (1, 'table_count', '20', '餐厅桌位数量', '2025-02-26 17:20:58', '2025-02-26 17:20:58');
INSERT INTO `settings` VALUES (2, 'delivery_fee', '5', '外卖配送费', '2025-02-26 17:20:58', '2025-02-26 17:20:58');

-- ----------------------------
-- Table structure for system_settings
-- ----------------------------
DROP TABLE IF EXISTS `system_settings`;
CREATE TABLE `system_settings`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `setting_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `setting_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `setting_key`(`setting_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_settings
-- ----------------------------
INSERT INTO `system_settings` VALUES (1, 'table_count', '20', '餐厅桌位数量');
INSERT INTO `system_settings` VALUES (2, 'delivery_fee', '5', '外卖配送费（元）');

-- ----------------------------
-- Table structure for us
-- ----------------------------
DROP TABLE IF EXISTS `us`;
CREATE TABLE `us`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `us_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `psd` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` int(11) NULL DEFAULT NULL,
  `phone` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `balance` decimal(10, 2) NOT NULL DEFAULT 99999.00 COMMENT '用户余额',
  `total_consumption` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '总消费金额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `us_name`(`us_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of us
-- ----------------------------
INSERT INTO `us` VALUES (2, 'nhm', '123456', 0, '12345678002', '管理员地址 2', 99999.00, 0.00);
INSERT INTO `us` VALUES (3, 'wangzeyu2', '123456', 0, '1234567890', '地址 15', 99999.00, 0.00);
INSERT INTO `us` VALUES (4, '王晓', '612407', 0, '1234567890', '地址 7', 99999.00, 0.00);
INSERT INTO `us` VALUES (5, '3孙馨', '899745', 0, '1234567890', '地址 19', 99999.00, 0.00);
INSERT INTO `us` VALUES (6, '4李婷', '346802', 0, '1234567890', '地址 4', 99999.00, 0.00);
INSERT INTO `us` VALUES (7, '5赵瑶', '710596', 0, '1234567890', '地址 12', 99999.00, 0.00);
INSERT INTO `us` VALUES (8, '6钱宏', '187542', 0, '1234567890', '地址 18', 99999.00, 0.00);
INSERT INTO `us` VALUES (9, '7周文', '932058', 0, '1234567890', '地址 0', 99999.00, 0.00);
INSERT INTO `us` VALUES (11, '郑雨', '639240', 0, '1234567890', '地址 6', 99999.00, 0.00);
INSERT INTO `us` VALUES (12, '10王梦', '105374', 0, '1234567890', '地址 9', 99999.00, 0.00);
INSERT INTO `us` VALUES (13, '11赵宇', '410593', 0, '1234567890', '地址 3', 99999.00, 0.00);
INSERT INTO `us` VALUES (14, '12孙阳', '238790', 0, '1234567890', '地址 17', 99999.00, 0.00);
INSERT INTO `us` VALUES (15, '13李阳', '679102', 0, '1234567890', '地址 16', 99999.00, 0.00);
INSERT INTO `us` VALUES (16, '14周阳', '853721', 0, '1234567890', '地址 2', 99999.00, 0.00);
INSERT INTO `us` VALUES (17, '15王阳', '916045', 0, '1234567890', '地址 8', 99999.00, 0.00);
INSERT INTO `us` VALUES (18, '16吴阳', '520937', 0, '1234567890', '地址 1', 99999.00, 0.00);
INSERT INTO `us` VALUES (19, '17冯阳', '794601', 0, '1234567890', '地址 20', 99999.00, 0.00);
INSERT INTO `us` VALUES (20, '18张阳', '187324', 0, '1234567890', '地址 11', 99999.00, 0.00);
INSERT INTO `us` VALUES (21, '19王阳', '462508', 0, '1234567890', '地址 5', 99999.00, 0.00);
INSERT INTO `us` VALUES (22, '20孙阳', '895316', 0, '1234567890', '地址 14', 99999.00, 0.00);
INSERT INTO `us` VALUES (23, '21吴阳', '630459', 0, '1234567890', '地址 10', 99999.00, 0.00);
INSERT INTO `us` VALUES (24, '22赵阳', '215780', 0, '1234567890', '地址 18', 99999.00, 0.00);
INSERT INTO `us` VALUES (25, '23钱阳', '947261', 0, '1234567890', '地址 3', 99999.00, 0.00);
INSERT INTO `us` VALUES (26, '24王阳', '826073', 0, '1234567890', '地址 12', 99999.00, 0.00);
INSERT INTO `us` VALUES (27, '25李阳', '531498', 0, '1234567890', '地址 16', 99999.00, 0.00);
INSERT INTO `us` VALUES (28, '26冯阳', '674289', 0, '1234567890', '地址 4', 99999.00, 0.00);
INSERT INTO `us` VALUES (29, '27周阳', '890372', 0, '1234567890', '地址 1', 99999.00, 0.00);
INSERT INTO `us` VALUES (30, '28张阳', '327645', 0, '1234567890', '地址 15', 99999.00, 0.00);
INSERT INTO `us` VALUES (31, '29孙阳', '549821', 0, '1234567890', '地址 13', 99999.00, 0.00);
INSERT INTO `us` VALUES (32, '30吴阳', '706284', 0, '1234567890', '地址 7', 99999.00, 0.00);
INSERT INTO `us` VALUES (33, '31李梦', '438716', 0, '1234567890', '地址 19', 99999.00, 0.00);
INSERT INTO `us` VALUES (34, '32王文', '925047', 0, '1234567890', '地址 8', 99999.00, 0.00);
INSERT INTO `us` VALUES (35, '33赵婷', '680231', 0, '1234567890', '地址 20', 99999.00, 0.00);
INSERT INTO `us` VALUES (36, '34钱雨', '512497', 0, '1234567890', '地址 9', 99999.00, 0.00);
INSERT INTO `us` VALUES (37, '35周宏', '346718', 0, '1234567890', '地址 6', 99999.00, 0.00);
INSERT INTO `us` VALUES (38, '36李梦', '879406', 0, '1234567890', '地址 2', 99999.00, 0.00);
INSERT INTO `us` VALUES (39, '37周梦', '888888', 0, '1234567890', '地址 3', 99999.00, 0.00);
INSERT INTO `us` VALUES (40, '38张晓', '777777', 0, '1234567890', '地址 4', 99999.00, 0.00);
INSERT INTO `us` VALUES (41, '39李文', '666666', 0, '1234567890', '地址 5', 99999.00, 0.00);
INSERT INTO `us` VALUES (42, '王雨', '555555', 0, '1234567890', '地址 6', 99999.00, 0.00);
INSERT INTO `us` VALUES (43, 'zhaoliu8', '123', 0, NULL, NULL, 99999.00, 0.00);
INSERT INTO `us` VALUES (46, 'heiliu1', '12345', 0, NULL, NULL, 99999.00, 0.00);
INSERT INTO `us` VALUES (48, 'newUser10', '123456', 0, NULL, NULL, 99999.00, 0.00);
INSERT INTO `us` VALUES (49, 'user100', '123456', 0, NULL, NULL, 99999.00, 0.00);
INSERT INTO `us` VALUES (51, '99999', '123456', 0, NULL, NULL, 99999.00, 0.00);
INSERT INTO `us` VALUES (52, '王泽宇', '123456', NULL, '12123', '12345', 99999.00, 0.00);
INSERT INTO `us` VALUES (53, 'testuser', '$2a$10$q0mUVXFZUKE2y0aOfURPSeIFp0bjdPRwmQHzT1Htq4t0FamhJoeMK', 0, '13800138000', 'Test Address', 99999.00, 0.00);
INSERT INTO `us` VALUES (54, 'testuser2', '$2a$10$kI1mOgz33nqKLHjYJyakK.G4e9cW8cZAhlH7g9ov3TpBltXL3b3UO', 0, '13800138001', 'Test Address 2', 99999.00, 0.00);
INSERT INTO `us` VALUES (55, 'newuser123', '$2a$10$IhjMzqkS8Uf/3c6w8pIjbec8Z.Hu8tfw8FdbdkSgHWJVgqPUsTTf2', 0, '13800138002', 'Test Address', 99999.00, 0.00);
INSERT INTO `us` VALUES (56, 'newuser', '$2a$10$eppVCWlNXP6OpJj6Kr.YVejPuNMftV7QpXIL/q241zJTS767xArKu', 0, '13800138000', 'Test Address', 99999.00, 0.00);
INSERT INTO `us` VALUES (57, 'wzy', '$2a$10$.ZkK6aG0iOeZ4kl2Vw5LPOZbBJ/0Gt9k5BEKJfoHOiU3JOMUGbYay', 0, '13319381001', '江西理工大学', 99980.55, 18.45);
INSERT INTO `us` VALUES (62, 'admin', '$2a$10$OQua4X3lLdNWbHTqRAwuae0s1kHyMcN/6YiCM7WlrSZ0mRE1bCej.', -1, '13800000000', '系统管理员', 0.00, 0.00);

SET FOREIGN_KEY_CHECKS = 1;
