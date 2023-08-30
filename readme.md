# spring-boot-starter-rxtx

## 3.x版本新功能 [2.x版本文档](https://github.com/han1396735592/spring-boot-starter-rxtx/tree/2.1.0)

- 支持别名配置
- 支持动态更换串口名称
- 支持热插拔错误处理
## 使用方法

- 普通項目请使用 [common-rxtx](https://github.com/han1396735592/common-rxtx)

1. 引入依赖
 ```xml
      <dependency>
         <groupId>cn.qqhxj.rxtx</groupId>
         <artifactId>spring-boot-starter-rxtx</artifactId>
         <version>3.1.0-RELEASE</version>
     </dependency>
 ```
2. 启动串口自动配置 `@EnableSerialPortAutoConfig`

3. 配置串口

- 注解和配置文件重复配置可能会配置出错

> - 注解配置 
```java
//@EnableSerialPorts({
//        @EnableSerialPort(port = "COM4",value = "串口别名"),
////        @EnableSerialPort(port = "COM5")
//})
//@EnableSerialPort(port = "COM1",value = "串口别名")
//@EnableSerialPort(port = "COM1")
```
> - 配置文件配置
>
```yml
serialport:
   config:
   - port: COM4
     alias: 温湿度
     baud: 9600
   - port: COM2
     alias: 默认串口2
 ```

3. 串口数据读取器配置（可选）

- 一个串口只能配置一个,不支持绑定多个串口上
- 使用 @SerialPortBinder(value = "COM2") 绑定到指定串口
- `AnyDataReader` 读取一切的数据 (默认配置)
- `ConstLengthSerialReader` 读取定长的数据
- `VariableLengthSerialReader` 读取有前后标识字符的数据
- `LiveControlSerialReader` 读取有开始位、数据长度的数据

  大家还可以按照自己的协议实现新的数据解析器
    - 需要继承`BaseSerialReader`
    - 不要忘记要加入到spring的IOC容器中，才能对数据进行处理哦

4. 数据解析器配置（可选）
```java
@SerialPortBinder("串口别名")
@Component
public class StringSerialDataParser implements SerialDataParser<String> {
  @Override
  public String parse(byte[] bytes,AbstractSerialContext serialContext) {
      return new String(bytes);
  }
}
``` 
5. 配置数据处理器
```java
@SerialPortBinder("串口别名")
@Component
public class XXXProcessor implements SerialDataProcessor<String> {
  @Override
  public void processor(String s,AbstractSerialContext serialContext) {
      System.out.println(s);
  }
}
```  
 

6. 获取 串口上下文 `SerialContext`

```java
@Qualifier(value = "串口别名.SerialContext")
@Autowired
private SerialContext serialContext;
//或者
@Lazy
@Resource
@Qualifier("串口别名.SerialContext");
private SerialContext serialContext;
```

7. 串口上下文事件监听器（可选）

```java
@Slf4j
@Component
//@SerialPortBinder("串口别名")
public class SerialContextEventListenerXXX implements SerialContextEventListener {
    @Override
    public void connected(AbstractSerialContext serialContext) {
        log.info("{} connected", serialContext.getSerialPort().getSerialPortInstance().getName());
    }
}
```
8. 启动

```java

@SpringBootApplication
public class RxtxDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(RxtxDemoApplication.class, args);
        while (true) {
            ;
        }
    }
}
```   
