-- =====================================================
-- 智慧医养大数据公共服务平台 · 示例数据（医生端联调用）
-- 示例医生账号：13800138000 / 123456
-- =====================================================

-- 医生账号（已审核，密码 123456 加密后）
INSERT IGNORE INTO doctor_account (id, phone, password, name, doctor_type, title, dept, org_id, avatar, status, create_time, update_time, deleted)
VALUES (30001, '13800138000', 'bc6ebe5d9aeefa86356c5f307099656491e3dde1a6b2d1ee4a5e4966f502c94d', '王医生', 1, '主治医师', '全科', 1001, 'https://xxx/doctor.png', 1, NOW(), NOW(), 0);

-- 医生资质证书
INSERT IGNORE INTO doctor_qualification (id, doctor_id, cert_type, cert_no, cert_img_url, valid_date, create_time)
VALUES (1, 30001, '执业医师资格证', 'ZY20260001', 'https://xxx/cert1.png', '2030-12-31', NOW());

-- 老人标签
INSERT IGNORE INTO elder_tag (id, tag_name, tag_type, description, color, create_time) VALUES
(1, '高血压', 1, '高血压重点人群', '#ff4d4f', NOW()),
(2, '糖尿病', 1, '糖尿病重点人群', '#fa8c16', NOW()),
(3, '独居老人', 2, '独居需重点关注', '#722ed1', NOW()),
(4, '失能老人', 2, '失能需照护', '#13c2c2', NOW());

-- 老人档案
INSERT IGNORE INTO elder_profile (id, name, id_card, phone, gender, birth_date, age, living_type, address, emergency_contact, emergency_phone, medical_history, create_by, create_time, update_time, deleted) VALUES
(1, '张大爷', '510101194501011234', '13900000001', 1, '1945-01-01', 81, 1, '成都市成华区某某小区1栋', '张小明', '13911110001', '高血压、糖尿病', 30001, NOW(), NOW(), 0),
(2, '李奶奶', '510101195003025678', '13900000002', 2, '1950-03-02', 76, 4, '成都市武侯区某某街道', '李小红', '13911110002', '冠心病', 30001, NOW(), NOW(), 0),
(3, '王爷爷', '510101194808081234', '13900000003', 1, '1948-08-08', 78, 3, '成都市锦江区某某养老院', '王建军', '13911110003', NULL, 30001, NOW(), NOW(), 0);

-- 老人健康档案
INSERT IGNORE INTO health_record (id, elder_id, record_type, height, weight, blood_pressure, heart_rate, blood_sugar, record_date, description, create_time) VALUES
(1, 1, 1, 170.0, 65.5, '150/95', 78, 7.2, '2026-09-01', '血压偏高，注意服药', NOW()),
(2, 1, 2, 170.0, 65.0, '145/90', 76, 6.8, '2026-08-15', '血压有所下降', NOW());

-- 老人家属联系人
INSERT IGNORE INTO family_contact (id, elder_id, name, relation, phone, is_default) VALUES
(1, 1, '张小明', '儿子', '13911110001', 1),
(2, 2, '李小红', '女儿', '13911110002', 1);

-- 老人-标签关联
INSERT IGNORE INTO elder_tag_relation (id, elder_id, tag_id, create_time) VALUES
(1, 1, 1, NOW()),
(2, 1, 2, NOW()),
(3, 2, 3, NOW());

-- 评估模板
INSERT IGNORE INTO assessment_template (id, template_name, assess_type, description, items_json, status, create_time) VALUES
(1, '老年人能力评估表', 1, '日常生活活动能力评估', '[{"itemId":1,"title":"进食"},{"itemId":2,"title":"穿衣"},{"itemId":3,"title":"行走"}]', 1, NOW()),
(2, '老年人健康风险评估表', 2, '慢性病风险评估', '[{"itemId":1,"title":"血压"},{"itemId":2,"title":"血糖"},{"itemId":3,"title":"用药"}]', 1, NOW());

-- 随访计划
INSERT IGNORE INTO follow_up_plan (id, elder_id, doctor_id, plan_name, follow_up_type, start_date, end_date, frequency, content, status, create_time, update_time, deleted) VALUES
(1, 1, 30001, '高血压随访计划', 1, '2026-09-01', '2026-12-31', '每月一次', '定期测量血压，指导用药', 1, NOW(), NOW(), 0);

-- 随访记录
INSERT IGNORE INTO follow_up_record (id, plan_id, elder_id, doctor_id, follow_up_date, content, result, next_follow_up_date, create_time) VALUES
(1, 1, 1, 30001, '2026-09-01', '测量血压 150/95，指导服药', '血压偏高，需持续观察', '2026-10-01', NOW());

-- 设备信息
INSERT IGNORE INTO device_info (id, device_name, device_type, device_no, elder_id, status, online_status, last_online_time, location, remark, create_time) VALUES
(1, '智能血压计', '血压计', 'DEV-BP-001', 1, 1, 1, NOW(), '张大爷家中', NULL, NOW()),
(2, '智能手环', '手环', 'DEV-BAND-002', 2, 1, 0, DATE_SUB(NOW(), INTERVAL 1 DAY), '李奶奶家中', NULL, NOW()),
(3, '智能血糖仪', '血糖仪', 'DEV-GLU-003', NULL, 1, 0, NULL, '仓库', '未绑定', NOW());

-- 医生排班
INSERT IGNORE INTO doctor_schedule (id, doctor_id, schedule_date, shift_type, start_time, end_time, remark) VALUES
(1, 30001, '2026-09-08', 1, '08:00:00', '12:00:00', '早班'),
(2, 30001, '2026-09-09', 2, '12:00:00', '18:00:00', '中班'),
(3, 30001, '2026-09-10', 1, '08:00:00', '12:00:00', '早班');

-- 服务记录
INSERT IGNORE INTO service_record (id, elder_id, doctor_id, service_type, service_date, content, rating, remark, status, create_time) VALUES
(1, 1, 30001, 1, '2026-09-01', '上门巡诊，测量血压', 5, '服务很好', 1, NOW()),
(2, 2, 30001, 2, '2026-09-03', '电话随访用药情况', 4, '态度亲切', 1, NOW()),
(3, 1, 30001, 1, '2026-09-05', '健康宣教', 5, NULL, 1, NOW());
