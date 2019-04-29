## lcn-starter，使用指南,以lcn-app-order,lcn-app-product,lcn-man三个项目为例
#### 其中lcn-man为TX事务管理中心，lcn-app-order为事务发起方，lcn-app-product为下游事务参与方
#### 步骤1. lcn-man 添加依赖项
	<dependency>
       <groupId>org.lcn</groupId>
       <artifactId>lcn-manager-starter</artifactId>
       <version>0.0.1-SNAPSHOT</version>
	</dependency>
#### 步骤2. lcn-app-order,lcn-app-product添加依赖项
    <dependency>
        <groupId>org.lcn</groupId>
        <artifactId>lcn-manager-starter</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
#### 步骤3.项目中的使用
#### 3.1  lcn-man 在主启动类上加上注解@EnableLCNServer，如
     @EnableLCNServer
     @SpringBootApplication
     public class LCNServer {
       public static void main(String[] args) {
          SpringApplication.run(LCNServer.class, args);
        }
    }
 ##### application.yml上下文配置文件中新增如下内容，配置tx-manager信息（目前版本只支持放在classpath路径下的文件名一致的文件）
	lcn:
	  manager:
	    port: 9090
#### 3.2  lcn-app-order事务发起方，首先主启动类添加注解@EnableLCNClient和@EnableAspectJAutoProxy，如
      @SpringBootApplication
      @EnableAspectJAutoProxy
      @EnableLCNClient
      public class AppOrderApplication {
        public static void main(String[] args) {
          SpringApplication.run(AppOrderApplication.class, args);
        }
      }
#### 在service层方法上加注解@LubanTransactional(isStart=true)如
      @Autowired
      private HttpClient httpClient;//这里的HttpClient只能使用lcn-core-starter里面的
      @Override
      @Transactional
      @LCNTransactional(isStart=true)
      public void insert(Order record) {
        httpClient.get("http://127.0.0.1:8092/add?name=aaa");
        orderMapper.insert(record);
        //System.out.println(1/0);//这句代码用来测试事务效果，出异常则事务发起方和参与方都会回滚
      }
##### application.yml上下文配置文件中新增如下内容，配置tx-manager信息和开启AOP（目前版本只支持放在classpath路径下的文件名一致的文件）
      spring:
        aop:
          auto: true
      lcn:
      manager:
        server: 192.168.41.2
        port: 9090
##### 写一个单元测试类
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = { AppOrderApplication.class })
    public class AppTests {

      @Autowired
      private OrderService orderService;

      @Test
      public void test() {
        Order order = Order.builder().name("淘宝订单").remark("衣物").price(new BigDecimal(199)).build();
        orderService.insert(order);
      }
    }
#### 3.3  lcn-app-product事务参与方，首先主启动类添加注解@EnableLCNClient和@EnableAspectJAutoProxy，如
      @SpringBootApplication
      @EnableAspectJAutoProxy
      @EnableLCNClient
      public class AppProductApplication {
        public static void main(String[] args) {
          SpringApplication.run(AppProductApplication.class, args);
        }
      }
  ##### 在service层方法上加注解@LubanTransactional(isEnd=true)如
      @Override
      @Transactional
      @LCNTransactional(isEnd=true)
      public void insert(Product record) {
        productMapper.insert(record);
      }
##### application.yml上下文配置文件中新增如下内容，配置tx-manager信息和开启AOP（目前版本只支持放在classpath路径下的文件名一致的文件）
      spring:
        aop:
          auto: true
      lcn:
      manager:
        server: 192.168.41.2
        port: 9090
#### 启动lcn-man项目
#### 启动lcn-app-product项目
#### 启动lcn-app-order项目单元测试AppTests，观察事务回滚情况

