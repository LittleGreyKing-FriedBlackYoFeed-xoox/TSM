system structure
项目根目录/
│
├── src/
│   ├── main/
│   │   ├── java/                  # Java源代码
│   │   │   └── com/               # 公司域名的反写
│   │   │       └── 项目名/         # 项目名称
│   │   │           ├── config/    # 配置类（Spring配置）
│   │   │           ├── controller # Web控制器/REST API
│   │   │           ├── service/   # 服务层接口
│   │   │           │   └── impl/  # 服务层实现
│   │   │           ├── dao/       # 数据访问层（或repository）
│   │   │           ├── model/     # 数据模型
│   │   │           │   ├── entity # 数据库实体
│   │   │           │   ├── dto    # 数据传输对象
│   │   │           │   └── vo     # 视图对象
│   │   │           ├── util/      # 工具类
│   │   │           ├── exception/ # 自定义异常
│   │   │           └── Application.java # 启动类
│   │   │
│   │   ├── resources/             # 资源文件
│   │   │   ├── static/            # 静态资源 (CSS/JS/图片)
│   │   │   ├── templates/         # 模板文件 (Thymeleaf/Freemarker)
│   │   │   ├── application.yml    # 主配置文件
│   │   │   ├── application-dev.yml# 开发环境配置
│   │   │   ├── application-prod.yml# 生产环境配置
│   │   │   ├── logback-spring.xml # 日志配置
│   │   │   └── mapper/            # MyBatis映射文件（如使用）
│   │   │
│   │   └── webapp/                # WEB资源（传统项目）
│   │       └── WEB-INF/
│   │
│   └── test/                      # 测试代码
│       ├── java/                  # 测试源文件（相同包结构）
│       └── resources/             # 测试资源
│
├── target/                        # 构建输出目录（自动生成）
│   ├── classes/                   # 编译后的类文件
│   └── 项目名-0.0.1-SNAPSHOT.jar  # 生成的JAR包
│
├── pom.xml                        # Maven构建文件（或build.gradle）
├── Dockerfile                     # Docker镜像构建文件
├── .gitignore                     # Git忽略规则
├── README.md                      # 项目文档
└── Jenkinsfile                    # CI/CD流水线配置
