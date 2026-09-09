-- =============================================================
-- 老人端（登录 + 个人中心）数据库补丁
-- 执行前：先跑 yiyang.sql 建库建表
-- =============================================================
USE yiyang_platform;

-- -------------------------------------------------------------
-- 1. 主键改自增（yiyang.sql 里 BIGINT 没有 AUTO_INCREMENT）
--    其余 24 张表同理，谁用到谁补
-- -------------------------------------------------------------
ALTER TABLE elder_account MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE elder_profile MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE sys_message  MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

-- -------------------------------------------------------------
-- 2. 三端主键错开起步值（替代 sys_message 加 user_type 列的低成本方案）
--    避免「管理端账户ID=1」和「老人账户ID=1」的消息互相串
-- -------------------------------------------------------------
ALTER TABLE sys_user       AUTO_INCREMENT = 100000;  -- 管理端 1xxxxx
ALTER TABLE doctor_account AUTO_INCREMENT = 200000;  -- 医生端 2xxxxx
ALTER TABLE elder_account  AUTO_INCREMENT = 300000;  -- 老人端 3xxxxx

-- -------------------------------------------------------------
-- 3. 注册查重：手机号唯一（已存在重复数据时会报错，先清理再执行）
-- -------------------------------------------------------------
ALTER TABLE elder_account ADD UNIQUE KEY uk_bind_phone (bind_phone);

-- -------------------------------------------------------------
-- 4. 消息列表高频查询，补联合索引
-- -------------------------------------------------------------
ALTER TABLE sys_message ADD KEY idx_user_read (user_id, is_read, is_deleted);

-- -------------------------------------------------------------
-- 5. 测试数据（老人端联调用）
--    密码均为 123456，BCrypt 密文已生成好
-- -------------------------------------------------------------

-- 5.1 已通过审核的正常账户（用于登录）
INSERT INTO elder_account (id, account_no, password, bind_phone, auth_status, account_status, register_channel)
VALUES (300001, 'EL202609080001', '$2a$10$WzOMMeXUVtfaz6tECIzGJO9K/GrvR8.NJmVMd3sMlZCXiTL.FSgjm', '13800138000', 1, 1, 1);

INSERT INTO elder_profile (id, elder_no, account_id, name, gender, birth_date, id_card, phone, living_type, status)
VALUES (300001, 'EL202609080001', 300001, '张桂芳', 2, '1948-03-12', '510107194803120026', '13800138000', 1, 1);

-- 5.2 待审核账户（用于验证「账号待审核」拦截）
INSERT INTO elder_account (id, account_no, password, bind_phone, auth_status, account_status, register_channel)
VALUES (300002, 'EL202609080002', '$2a$10$WzOMMeXUVtfaz6tECIzGJO9K/GrvR8.NJmVMd3sMlZCXiTL.FSgjm', '13800138001', 0, 1, 1);

INSERT INTO elder_profile (id, elder_no, account_id, name, gender, birth_date, id_card, phone, living_type, status)
VALUES (300002, 'EL202609080002', 300002, '李国栋', 1, '1950-07-08', '510107195007080019', '13800138001', 1, 1);

-- 5.3 已冻结账户（用于验证「账号已冻结」拦截）
INSERT INTO elder_account (id, account_no, password, bind_phone, auth_status, account_status, register_channel)
VALUES (300003, 'EL202609080003', '$2a$10$WzOMMeXUVtfaz6tECIzGJO9K/GrvR8.NJmVMd3sMlZCXiTL.FSgjm', '13800138002', 1, 2, 1);

INSERT INTO elder_profile (id, elder_no, account_id, name, gender, birth_date, id_card, phone, living_type, status)
VALUES (300003, 'EL202609080003', 300003, '王秀英', 2, '1945-11-23', '510107194511230042', '13800138002', 4, 1);

-- -------------------------------------------------------------
-- 6. 消息测试数据（挂在 300001 账户下）
-- -------------------------------------------------------------
INSERT INTO sys_message (user_id, msg_type, title, content, is_read) VALUES
(300001, 1, '平台上线公告',   '智慧医养平台已正式上线，欢迎使用。', 0),
(300001, 2, '服务工单提醒',   '您预约的上门服务已排期，请注意查收。', 0),
(300001, 3, '账户审核通过',   '您的账户已通过审核，可正常使用全部功能。', 1),
(300001, 1, '健康预警提醒',   '近期血压数据偏高，建议联系家庭医生。', 0);

-- -------------------------------------------------------------
-- 7. 标签测试数据（验证老人档案的标签聚合）
-- -------------------------------------------------------------
INSERT INTO elder_tag (id, tag_name, tag_level, description, status)
VALUES (1, '高龄', 2, '80周岁以上', 1),
       (2, '独居', 2, '独自居住', 1),
       (3, '慢病', 1, '慢性病患者', 1);

INSERT INTO elder_tag_relation (id, elder_id, tag_id) VALUES
(1, 300001, 1),
(2, 300001, 2),
(3, 300001, 3);

-- -------------------------------------------------------------
-- 8. 健康档案 / 家属联系人测试数据
-- -------------------------------------------------------------
INSERT INTO health_record (id, elder_id, record_type, disease_name, icd_code, diagnose_date, hospital, detail, medication)
VALUES (1, 300001, 1, '高血压', 'I10', '2015-06-01', '成都市第三人民医院', '长期服药控制，血压稳定', '苯磺酸氨氯地平片'),
       (2, 300001, 2, '青霉素过敏', NULL, NULL, NULL, '皮试阳性，禁用青霉素类药物', NULL);

INSERT INTO family_contact (id, elder_id, name, relation, phone, is_primary, address)
VALUES (1, 300001, '张伟', '儿子', '13900139000', 1, '成都市武侯区xxx小区');
