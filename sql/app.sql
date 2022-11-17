/*
 Navicat Premium Data Transfer

 Source Server         : app
 Source Server Type    : MySQL
 Source Server Version : 50739
 Source Host           : 81.68.69.163:3306
 Source Schema         : app

 Target Server Type    : MySQL
 Target Server Version : 50739
 File Encoding         : 65001

 Date: 17/11/2022 19:23:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `dict_label` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `dict_sort` int(11) NOT NULL DEFAULT 0,
  `dict_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `dict_value` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_default` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N',
  `remark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `css_class` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `list_class` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `k_dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, NULL, NULL, NULL, NULL, '男', 1, 'sys_user_sex', '0', 'Y', '性别男', '0', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (2, NULL, NULL, NULL, NULL, '女', 2, 'sys_user_sex', '1', 'N', '性别女', '0', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (3, NULL, NULL, NULL, NULL, '未知', 3, 'sys_user_sex', '2', 'N', '性别未知', '0', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (4, NULL, NULL, NULL, NULL, '正常', 1, 'sys_normal_disable', '0', 'Y', '正常状态', '0', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (5, NULL, NULL, NULL, NULL, '停用', 2, 'sys_normal_disable', '1', 'N', '停用状态', '0', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (6, NULL, NULL, NULL, NULL, '显示', 1, 'sys_show_hide', '0', 'Y', '显示菜单', '0', NULL, 'primary');
INSERT INTO `sys_dict_data` VALUES (7, NULL, NULL, NULL, NULL, '隐藏', 2, 'sys_show_hide', '1', 'N', '隐藏菜单', '0', NULL, 'danger');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `dict_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `dict_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `remark` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `k_dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------

-- ----------------------------
-- Table structure for sys_login_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_info`;
CREATE TABLE `sys_login_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `browser` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ipaddr` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `login_location` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `login_time` datetime NULL DEFAULT NULL,
  `msg` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `os` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `k_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_info
-- ----------------------------
INSERT INTO `sys_login_info` VALUES (1, NULL, '2022-10-14 08:41:10', NULL, '2022-10-14 08:41:10', 'MSEdge', '127.0.0.1', '内网IP', NULL, '登录成功', 'Windows 10 or Windows Server 2016', '0', 'admin');
INSERT INTO `sys_login_info` VALUES (2, NULL, '2022-10-14 09:04:34', NULL, '2022-10-14 09:04:34', 'MSEdge', '127.0.0.1', '内网IP', NULL, '登录成功', 'Windows 10 or Windows Server 2016', '0', 'admin');
INSERT INTO `sys_login_info` VALUES (3, NULL, '2022-10-18 16:36:35', NULL, '2022-10-18 16:36:35', 'MSEdge', '127.0.0.1', '内网IP', NULL, '登录成功', 'Windows 10 or Windows Server 2016', '0', 'admin');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `component` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `menu_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `menu_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `order_num` int(11) NOT NULL DEFAULT 0,
  `parent_id` bigint(20) NOT NULL DEFAULT 0,
  `path` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `perms` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `visible` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `remark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uk_menu_name`(`menu_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, NULL, NULL, NULL, NULL, NULL, 'system', '系统管理', 'M', 1, 0, 'system', NULL, '0', '0', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, NULL, NULL, NULL, NULL, 'system/user/index', 'user', '用户管理', 'C', 1, 1, 'user', 'system:user:list', '0', '0', '用户管理菜单');
INSERT INTO `sys_menu` VALUES (3, NULL, NULL, NULL, NULL, 'system/role/index', 'peoples', '角色管理', 'C', 2, 1, 'role', 'system:role:list', '0', '0', '角色管理菜单');
INSERT INTO `sys_menu` VALUES (4, NULL, '2022-09-24 16:58:54', NULL, '2022-09-24 16:58:54', 'system/menu/index', 'tree-table', '菜单管理', 'C', 3, 1, 'menu', 'system:menu:list', '0', '0', '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (16, NULL, '2022-09-26 09:47:09', NULL, '2022-09-26 09:47:09', NULL, NULL, '查询用户', 'F', 99, 2, NULL, 'system:user:query', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (17, NULL, '2022-09-26 09:47:28', NULL, '2022-09-26 09:47:28', NULL, NULL, '重置密码', 'F', 98, 2, NULL, 'system:user:resetPwd', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (18, NULL, '2022-09-26 09:47:49', NULL, '2022-09-26 09:47:49', NULL, NULL, '删除用户', 'F', 97, 2, NULL, 'system:user:remove', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (19, NULL, '2022-09-26 09:48:05', NULL, '2022-09-26 09:48:05', NULL, NULL, '编辑用户', 'F', 96, 2, NULL, 'system:user:edit', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (20, NULL, '2022-09-26 09:48:24', NULL, '2022-09-26 09:48:24', NULL, NULL, '新增用户', 'F', 95, 2, NULL, 'system:user:add', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (21, NULL, '2022-09-26 09:48:53', NULL, '2022-09-26 09:50:00', NULL, NULL, '用户列表', 'F', 94, 2, NULL, 'system:user:list', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (22, NULL, '2022-09-26 09:50:25', NULL, '2022-09-26 09:50:25', NULL, NULL, '角色列表', 'F', 99, 3, NULL, 'system:role:list', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (23, NULL, '2022-09-26 09:50:43', NULL, '2022-09-26 09:50:43', NULL, NULL, '新增角色', 'F', 98, 3, NULL, 'system:role:add', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (24, NULL, '2022-09-26 09:50:58', NULL, '2022-09-26 09:50:58', NULL, NULL, '更新角色', 'F', 97, 3, NULL, 'system:role:edit', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (25, NULL, '2022-09-26 09:51:23', NULL, '2022-09-26 09:51:23', NULL, NULL, '编辑角色', 'F', 96, 3, '', 'system:role:edit', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (26, NULL, '2022-09-26 09:51:37', NULL, '2022-09-26 09:51:37', NULL, NULL, '删除角色', 'F', 95, 3, NULL, 'system:role:remove', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (27, NULL, '2022-09-26 09:51:59', NULL, '2022-09-26 09:51:59', NULL, NULL, '菜单列表', 'F', 99, 4, NULL, 'system:menu:list', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (28, NULL, '2022-09-26 09:52:56', NULL, '2022-09-26 09:52:56', NULL, NULL, '编辑菜单', 'F', 98, 4, NULL, 'system:menu:edit', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (29, NULL, '2022-09-26 09:53:19', NULL, '2022-09-26 09:53:19', NULL, NULL, '新增菜单', 'F', 97, 4, NULL, 'system:menu:add', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (30, NULL, '2022-09-26 09:53:32', NULL, '2022-09-26 09:53:32', NULL, NULL, '删除菜单', 'F', 96, 4, NULL, 'system:menu:remove', '0', '0', NULL);
INSERT INTO `sys_menu` VALUES (31, NULL, '2022-10-14 09:17:56', NULL, '2022-10-14 09:18:20', 'system/dict/index', 'education', '字典管理', 'C', 4, 1, 'Dict', 'system:dict.list', '0', '0', NULL);

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `business_type` int(11) NULL DEFAULT NULL,
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `oper_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operator_type` int(11) NULL DEFAULT NULL,
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `role_key` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_sort` int(11) NOT NULL DEFAULT 0,
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `remark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_key`(`role_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, NULL, NULL, NULL, '2022-09-23 16:39:22', 'admin', '超级管理员', 0, '0', NULL);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `avatar` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `login_date` datetime NULL DEFAULT NULL,
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nickname` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sex` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `username` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, NULL, '2022-09-15 17:13:03', NULL, '2022-10-18 16:36:35', '/profile/2022/10/09/8N75ZS2k_ozs4Q7e.jpeg', '2022-10-18 16:36:34', '127.0.0.1', '鲍勃', '$2a$10$cycFzFLrcMBJdmo8oHU/x.9X0Q8vPrduNtwGolA6t0ccKO12dNK.C', '18888888888', '超级管理员', '0', '0', 'admin');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
