-- =====================================================
-- 智慧医养大数据公共服务平台 · 数据库表结构（医生端）
-- 数据库：yiyang_platform   字符集：utf8mb4
-- =====================================================

-- 医生账号
CREATE TABLE IF NOT EXISTS doctor_account (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    phone        VARCHAR(20)  NOT NULL COMMENT '手机号(登录账号)',
    password     VARCHAR(128) NOT NULL COMMENT '密码(加密存储)',
    name         VARCHAR(50)  NOT NULL COMMENT '姓名',
    doctor_type  TINYINT      NOT NULL DEFAULT 1 COMMENT '医生类型:1家庭医生 2专科医生 3康复师 4护理师',
    title        VARCHAR(50)  DEFAULT NULL COMMENT '职称',
    dept         VARCHAR(50)  DEFAULT NULL COMMENT '科室',
    org_id       BIGINT       DEFAULT NULL COMMENT '所属机构ID',
    avatar       VARCHAR(255) DEFAULT NULL COMMENT '头像',
    status       TINYINT      NOT NULL DEFAULT 0 COMMENT '状态:0待审核 1已审核 2已拒绝 3冻结',
    create_time  DATETIME     DEFAULT NULL COMMENT '创建时间',
    update_time  DATETIME     DEFAULT NULL COMMENT '更新时间',
    deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除:0未删除 1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_phone (phone)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '医生账号';

-- 医生资质证书
CREATE TABLE IF NOT EXISTS doctor_qualification (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    doctor_id    BIGINT       NOT NULL COMMENT '医生ID',
    cert_type    VARCHAR(50)  DEFAULT NULL COMMENT '证书类型',
    cert_no      VARCHAR(100) DEFAULT NULL COMMENT '证书编号',
    cert_img_url VARCHAR(255) DEFAULT NULL COMMENT '证书图片地址',
    valid_date   DATE         DEFAULT NULL COMMENT '有效期',
    create_time  DATETIME     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_doctor_id (doctor_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '医生资质证书';

-- 老人档案
CREATE TABLE IF NOT EXISTS elder_profile (
    id                BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    name              VARCHAR(50)   NOT NULL COMMENT '姓名',
    id_card           VARCHAR(18)   DEFAULT NULL COMMENT '身份证号',
    phone             VARCHAR(20)   DEFAULT NULL COMMENT '联系电话',
    gender            TINYINT       DEFAULT 1 COMMENT '性别:1男 2女',
    birth_date        DATE          DEFAULT NULL COMMENT '出生日期',
    age               INT           DEFAULT NULL COMMENT '年龄',
    living_type       TINYINT       DEFAULT NULL COMMENT '居住类型:1居家 2社区 3机构 4独居',
    address           VARCHAR(255)  DEFAULT NULL COMMENT '居住地址',
    photo             VARCHAR(255)  DEFAULT NULL COMMENT '照片地址',
    emergency_contact VARCHAR(50)   DEFAULT NULL COMMENT '紧急联系人',
    emergency_phone   VARCHAR(20)   DEFAULT NULL COMMENT '紧急联系电话',
    medical_history   VARCHAR(1000) DEFAULT NULL COMMENT '病史',
    remark            VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    create_by         BIGINT        DEFAULT NULL COMMENT '创建医生ID',
    create_time       DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_time       DATETIME      DEFAULT NULL COMMENT '更新时间',
    deleted           TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除:0未删除 1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_id_card (id_card)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '老人档案';

-- 老人健康档案
CREATE TABLE IF NOT EXISTS health_record (
    id             BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    elder_id       BIGINT        NOT NULL COMMENT '老人ID',
    record_type    TINYINT       DEFAULT NULL COMMENT '记录类型',
    height         DECIMAL(5, 1) DEFAULT NULL COMMENT '身高(cm)',
    weight         DECIMAL(5, 1) DEFAULT NULL COMMENT '体重(kg)',
    blood_pressure VARCHAR(20)   DEFAULT NULL COMMENT '血压',
    heart_rate     INT           DEFAULT NULL COMMENT '心率',
    blood_sugar    DECIMAL(5, 1) DEFAULT NULL COMMENT '血糖',
    record_date    DATE          DEFAULT NULL COMMENT '记录日期',
    description    VARCHAR(1000) DEFAULT NULL COMMENT '描述',
    create_time    DATETIME      DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_elder_id (elder_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '老人健康档案';

-- 老人家属联系人
CREATE TABLE IF NOT EXISTS family_contact (
    id         BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    elder_id   BIGINT      NOT NULL COMMENT '老人ID',
    name       VARCHAR(50) DEFAULT NULL COMMENT '姓名',
    relation   VARCHAR(20) DEFAULT NULL COMMENT '关系',
    phone      VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    is_default TINYINT     NOT NULL DEFAULT 0 COMMENT '是否默认:0否 1是',
    PRIMARY KEY (id),
    KEY idx_elder_id (elder_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '老人家属联系人';

-- 老人标签
CREATE TABLE IF NOT EXISTS elder_tag (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    tag_name    VARCHAR(50)  NOT NULL COMMENT '标签名称',
    tag_type    TINYINT      DEFAULT NULL COMMENT '标签类型',
    description VARCHAR(255) DEFAULT NULL COMMENT '描述',
    color       VARCHAR(20)  DEFAULT NULL COMMENT '标签颜色',
    create_time DATETIME     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '老人标签';

-- 老人-标签关联
CREATE TABLE IF NOT EXISTS elder_tag_relation (
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    elder_id    BIGINT   NOT NULL COMMENT '老人ID',
    tag_id      BIGINT   NOT NULL COMMENT '标签ID',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_elder_tag (elder_id, tag_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '老人-标签关联';

-- 评估模板
CREATE TABLE IF NOT EXISTS assessment_template (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    assess_type   TINYINT      NOT NULL COMMENT '评估类型',
    description   VARCHAR(500) DEFAULT NULL COMMENT '描述',
    items_json    TEXT         COMMENT '评估题目(JSON)',
    status        TINYINT      NOT NULL DEFAULT 1 COMMENT '状态:1启用 0停用',
    create_time   DATETIME     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '评估模板';

-- 评估报告
CREATE TABLE IF NOT EXISTS assessment_report (
    id          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    elder_id    BIGINT        NOT NULL COMMENT '老人ID',
    template_id BIGINT        DEFAULT NULL COMMENT '模板ID',
    assess_type TINYINT       DEFAULT NULL COMMENT '评估类型',
    assess_date DATE          DEFAULT NULL COMMENT '评估日期',
    total_score DECIMAL(6, 1) DEFAULT NULL COMMENT '总分',
    level       VARCHAR(20)   DEFAULT NULL COMMENT '评估等级',
    conclusion  VARCHAR(1000) DEFAULT NULL COMMENT '评估结论',
    report_url  VARCHAR(255)  DEFAULT NULL COMMENT '报告文件地址',
    doctor_id   BIGINT        DEFAULT NULL COMMENT '评估医生ID',
    create_time DATETIME      DEFAULT NULL COMMENT '创建时间',
    deleted     TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除:0未删除 1已删除',
    PRIMARY KEY (id),
    KEY idx_elder_id (elder_id),
    KEY idx_doctor_id (doctor_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '评估报告';

-- 评估答案明细
CREATE TABLE IF NOT EXISTS assessment_answer (
    id          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    report_id   BIGINT        NOT NULL COMMENT '报告ID',
    item_id     BIGINT        DEFAULT NULL COMMENT '题目ID',
    answer      VARCHAR(500)  DEFAULT NULL COMMENT '答案内容',
    score       DECIMAL(6, 1) DEFAULT NULL COMMENT '得分',
    create_time DATETIME      DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_report_id (report_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '评估答案明细';

-- 随访计划
CREATE TABLE IF NOT EXISTS follow_up_plan (
    id             BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    elder_id       BIGINT        NOT NULL COMMENT '老人ID',
    doctor_id      BIGINT        DEFAULT NULL COMMENT '医生ID',
    plan_name      VARCHAR(100)  DEFAULT NULL COMMENT '计划名称',
    follow_up_type TINYINT       DEFAULT NULL COMMENT '随访类型',
    start_date     DATE          DEFAULT NULL COMMENT '开始日期',
    end_date       DATE          DEFAULT NULL COMMENT '结束日期',
    frequency      VARCHAR(50)   DEFAULT NULL COMMENT '随访频率',
    content        VARCHAR(1000) DEFAULT NULL COMMENT '随访内容',
    status         TINYINT       NOT NULL DEFAULT 1 COMMENT '状态:1进行中 0已结束',
    create_time    DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_time    DATETIME      DEFAULT NULL COMMENT '更新时间',
    deleted        TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除:0未删除 1已删除',
    PRIMARY KEY (id),
    KEY idx_elder_id (elder_id),
    KEY idx_doctor_id (doctor_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '随访计划';

-- 随访记录
CREATE TABLE IF NOT EXISTS follow_up_record (
    id                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    plan_id             BIGINT        DEFAULT NULL COMMENT '随访计划ID',
    elder_id            BIGINT        NOT NULL COMMENT '老人ID',
    doctor_id           BIGINT        DEFAULT NULL COMMENT '医生ID',
    follow_up_date      DATE          DEFAULT NULL COMMENT '随访日期',
    content             VARCHAR(1000) DEFAULT NULL COMMENT '随访内容',
    result              VARCHAR(1000) DEFAULT NULL COMMENT '随访结果',
    next_follow_up_date DATE          DEFAULT NULL COMMENT '下次随访日期',
    create_time         DATETIME      DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_elder_id (elder_id),
    KEY idx_doctor_id (doctor_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '随访记录';

-- 设备信息
CREATE TABLE IF NOT EXISTS device_info (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    device_name      VARCHAR(100) DEFAULT NULL COMMENT '设备名称',
    device_type      VARCHAR(50)  DEFAULT NULL COMMENT '设备类型',
    device_no        VARCHAR(50)  DEFAULT NULL COMMENT '设备编号',
    elder_id         BIGINT       DEFAULT NULL COMMENT '绑定老人ID',
    status           TINYINT      NOT NULL DEFAULT 1 COMMENT '状态:1正常 2维修 3停用',
    online_status    TINYINT      NOT NULL DEFAULT 0 COMMENT '在线状态:0离线 1在线',
    last_online_time DATETIME     DEFAULT NULL COMMENT '最后在线时间',
    location         VARCHAR(255) DEFAULT NULL COMMENT '设备位置',
    remark           VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_time      DATETIME     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_elder_id (elder_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '设备信息';

-- 医生排班
CREATE TABLE IF NOT EXISTS doctor_schedule (
    id            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    doctor_id     BIGINT      NOT NULL COMMENT '医生ID',
    schedule_date DATE        NOT NULL COMMENT '排班日期',
    shift_type    TINYINT     DEFAULT NULL COMMENT '班次:1早班 2中班 3晚班',
    start_time    TIME        DEFAULT NULL COMMENT '开始时间',
    end_time      TIME        DEFAULT NULL COMMENT '结束时间',
    remark        VARCHAR(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_doctor_date (doctor_id, schedule_date)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '医生排班';

-- 服务记录
CREATE TABLE IF NOT EXISTS service_record (
    id           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    elder_id     BIGINT        NOT NULL COMMENT '老人ID',
    doctor_id    BIGINT        NOT NULL COMMENT '医生ID',
    service_type TINYINT       DEFAULT NULL COMMENT '服务类型',
    service_date DATE          DEFAULT NULL COMMENT '服务日期',
    content      VARCHAR(1000) DEFAULT NULL COMMENT '服务内容',
    rating       TINYINT       DEFAULT NULL COMMENT '评分(1-5)',
    remark       VARCHAR(500)  DEFAULT NULL COMMENT '评价备注',
    status       TINYINT       NOT NULL DEFAULT 1 COMMENT '状态:1已完成 0进行中',
    create_time  DATETIME      DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_elder_id (elder_id),
    KEY idx_doctor_id (doctor_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '服务记录';
