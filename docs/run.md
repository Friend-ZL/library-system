# 图书管理系统运行说明

## 技术栈

- 后端：JDK 17、Spring Boot 3.3.5、Spring Security、Spring Data JPA、JWT
- 数据库：MySQL 8.x
- 前端：Vue 3、Vite、Pinia、Element Plus、Axios

## 初始化数据库

```sql
SOURCE D:/github/demo/sql/schema.sql;
```

也可以在 MySQL 客户端中直接执行 `sql/schema.sql`。

默认数据库连接在 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/library_system
    username: root
    password: root
```

如果你的 MySQL 密码不是 `root`，先修改该配置。

## 启动后端

```powershell
cd D:\github\demo\backend
$env:JAVA_HOME='D:\Java\jdk-26.0.1'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
mvn spring-boot:run
```

项目目标版本是 JDK 17，使用更高版本 JDK 运行 Maven 也可以。

首次启动会自动创建默认管理员：

```text
用户名：admin
密码：admin123
```

## 启动前端

```powershell
cd D:\github\demo\frontend
npm install
npm run dev
```

浏览器访问：

```text
http://127.0.0.1:5173
```

## 主要功能

- 管理员登录
- 图书新增、编辑、删除、查询
- 分类新增、编辑、删除
- 用户新增、启用、禁用
- 普通用户借阅申请
- 管理员审批、拒绝、确认归还
- 我的借阅记录
- 管理端统计仪表盘
