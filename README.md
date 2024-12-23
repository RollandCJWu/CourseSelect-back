# 選課系統後端

## 使用語言及資料庫
Java 17
Maven
SQL Server

## 資料庫初始化
```
use Course


-- 刪除 foreign key
DECLARE @sql NVARCHAR(MAX) = '';

SELECT @sql = @sql + 'ALTER TABLE ' + QUOTENAME(OBJECT_NAME(fk.parent_object_id)) 
                   + ' DROP CONSTRAINT ' + QUOTENAME(fk.name) + ';' + CHAR(13)
FROM sys.foreign_keys fk;

EXEC sp_executesql @sql;

-- 刪除 tables
DROP TABLE if exists Students;
DROP TABLE if exists Teachers;
DROP TABLE if exists Lectures;
DROP TABLE if exists Students_Lectures;

-- 建立學生表
CREATE TABLE Students (
    studentId INT PRIMARY KEY IDENTITY(1,1),
    firstName NVARCHAR(50) NOT NULL,
    lastName NVARCHAR(50) NOT NULL,
	password VARCHAR(20) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE
);

-- 建立老師表
CREATE TABLE Teachers (
    teacherId INT PRIMARY KEY IDENTITY(1,1),
    firstName NVARCHAR(50) NOT NULL,
    lastName NVARCHAR(50) NOT NULL,
	password VARCHAR(20) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE
);

-- 建立課程表
CREATE TABLE Lectures (
    lectureId INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(100) NOT NULL,
    teacherId INT NOT NULL,
    FOREIGN KEY (teacherId) REFERENCES Teachers(teacherId),
);

-- 建立學生課程關聯
CREATE TABLE Students_Lectures (
    StudentId INT,
    LectureId INT,
    PRIMARY KEY (StudentId, LectureId),
    FOREIGN KEY (StudentId) REFERENCES Students(StudentId),
    FOREIGN KEY (LectureId) REFERENCES Lectures(LectureId)
);

-- 導入資料
INSERT INTO Students (firstName, lastName, password, email) VALUES 
('中中', '賴', 'abc' , 'lai123@example.com'),
('John', 'Doe', 'abc' , 'john.doe@example.com'),
('Jane', 'Smith', 'abc' , 'jane.smith@example.com'),
('Mike', 'Brown', 'abc' , 'mike.brown@example.com'),
('大明', '王', 'abc' , 'alice.johnson@example.com'),
('小美', '陳', 'abc' , 'tom.davis@example.com');

INSERT INTO Teachers (firstName, lastName, password, email) VALUES 
('sa', 'admin', 'sa' , 'sa@example.com'),
('暖暖', '黃', 'abcde' , 'huang123@example.com'),
('Emma', 'Williams', 'abcde' , 'emma.williams@example.com'),
('Oliver', 'Jones', 'abcde' , 'oliver.jones@example.com'),
('Sophia', 'Taylor', 'abcde' , 'sophia.taylor@example.com'),
('大偉', '吳', 'abcde' , 'liam.moore@example.com'),
('為中', '蔡', 'abcde' , 'isabella.white@example.com');

INSERT INTO Lectures (title, teacherId) VALUES
('Java 程式設計', 1),
('C++ 程式設計', 1),
('工程數學', 2),
('普通物理', 3),
('English Literature', 4),
('Data Structure', 5);
```
##資料庫Config
設定於 application.properties
```
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=Course;trustServerCertificate=true
spring.datasource.username=your-username
spring.datasource.password=your-password
```


## 已知問題
POST 和 PUT 相關功能由於資料庫關聯原因無法使用
由於前端網頁功能不全，請使用Postman或相關軟體測試

## API 端點
### 教師 (Teachers)
POST /teachers/login - 教師登入
GET /teachers - 取得所有教師
POST /teachers - 新增教師
GET /teachers/{id} - 根據 ID 查詢教師
PUT /teachers/{id} - 更新教師資料
DELETE /teachers/{id} - 刪除教師
### 學生 (Students)
POST /students/login - 學生登入
GET /students - 取得所有學生
POST /students - 新增學生
GET /students/{id} - 根據 ID 查詢學生
PUT /students/{id} - 更新學生資料
PUT /students/{id}/enroll/{lectureId} - 將學生註冊至指定課程
DELETE /students/{id} - 刪除學生
### 課程 (Lectures)
GET /lectures - 取得所有課程
POST /lectures - 新增課程
GET /lectures/{id} - 根據 ID 查詢課程
PUT /lectures/{id} - 更新課程資料
DELETE /lectures/{id} - 刪除課程
