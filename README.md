# hwadee

成都理工大学2024级软件工程专业华迪实训

项目：智慧医养大数据公共服务平台云端后台管理系统

---

## 全队统一约定（必读，别各写各的）

| 项 | 约定 |
|---|---|
| 基础路径 | `/api` |
| 端路径前缀 | 管理端 `/api/admin/**`、医生端 `/api/doctor/**`、老人端 `/api/elder/**` |
| userType | 1-管理端 2-医生端 3-老人端 |
| 实体包名 | `com.cdut.pojo` |
| 返回体 | `Result<T>`，**只有 200 / 500** 两种 code，失败原因放 `message` |
| 分页 | `PageResult<T>`，字段对齐 PageInfo：`{total, pages, pageNum, pageSize, list}` |
| 主键 | 数据库自增（不用雪花算法） |
| 逻辑删除 | 手写 `AND is_deleted = 0`（不引 MyBatis-Plus） |
| 密码 | BCrypt，注册/改密用 `encode()`，登录用 `matches()` |
| 认证 | JWT，Header `Authorization: Bearer {token}`，payload 含 `userId`、`userType` |
| 当前用户 | `UserContext.getUserId()`（ThreadLocal），不要再从 request 里取 |
| 业务异常 | `throw new BizException("提示语")`，全局处理器自动转 `Result.error(...)` |

### ★ userId 语义（最容易搞错）

JWT 里的 `userId` 是**账户ID**，不是档案ID：

| 端 | userId 来自 |
|---|---|
| 老人端 | `elder_account.id` |
| 医生端 | `doctor_account.id` |
| 管理端 | `sys_user.id` |

老人端查 `service_record` / `follow_up_record` / `assessment_report` / `elder_tag_relation`
这类业务表时，它们的 `elder_id` 是 **`elder_profile.id`（档案ID）**，必须先换算：

```java
Long accountId = UserContext.getUserId();
Long elderId   = profileMapper.selectElderIdByAccountId(accountId);
```

### ★ sys_message.user_id 语义

存**账户ID**（老人端 = `elder_account.id`）。三端主键已错开起步值，避免撞号：

```
sys_user       AUTO_INCREMENT = 100000
doctor_account AUTO_INCREMENT = 200000
elder_account  AUTO_INCREMENT = 300000
```

---

## 数据库

1. 先执行 `yiyang.sql`（建库建表，27 张表）
2. 再执行 `docs/sql/01-老人端初始化补丁.sql`（自增、索引、测试数据）

## 文档

- `docs/01-项目梳理-现状与问题清单.md` —— 项目全貌与基建分歧
- `docs/02-老人端登录与个人中心-开发规划.md` —— 老人端模块规划与接口设计

## 本地启动

```bash
mvn spring-boot:run
# 或 java -jar target/yiyang-Project-0.0.1-SNAPSHOT.jar
```

端口 `8080`，数据库连接串在 `application.properties`。
