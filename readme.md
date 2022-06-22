# spring-boot-starter-rxtx

spring boot 对串口的读取的快速方法
[demo](https://github.com/han1396735592/rxtx-demo)

## 与 1.0 版本的不同之处

- 支持了多串口操作
- maven 坐标名称有调整

## 使用方法

- 普通項目请使用 [common-rxtx](https://github.com/han1396735592/common-rxtx)

1. 引入依赖
   - 代码未发布到中心仓库 自行克隆打包使用
    - 代码引用了 [common-rxtx](https://github.com/han1396735592/common-rxtx) 的2.0.0-RELEASE版本也需要本地打包使用
    ```xml
     <dependency>
            <groupId>cn.qqhxj.rxtx</groupId>
            <artifactId>spring-boot-starter-rxtx</artifactId>
            <version>2.0.1-RELEASE</version>
     </dependency>
    ```
2. 启动串口自动配置 `@EnableSerialPortAutoConfig`

3. 配置串口

> - 注解配置 `@EnableSerialPort(value = "COM2", portName = "COM2")`
> - 配置文件配置
>
> ```yml
> serialport:
>   config:
>     - portName: COM1
>     - portName: COM2
> ```

3. 串口数据读取器配置（可选）

   默认配置了 数据开始为 `{`， 数据结束为`}` 的数据解析器（`VariableLengthSerialReader`） 系统还提供了以下四种数据读取器。
    - `AnyDataReader` 读取一切的数据
    - `ConstLengthSerialReader` 读取定长的数据
    - `VariableLengthSerialReader` 读取有前后标识字符的数据
    - `LiveControlSerialReader` 读取有开始位、数据长度的数据

   大家还可以按照自己的协议实现新的数据解析器
    - 需要实现`SerialReader`接口
    - 不要忘记要加入到spring的IOC容器中，才能对数据进行处理哦
4. 数据解析器配置（可选）

   默认配置了 字符串的数据解析器（将数据读取器读取的数据直接转为字符串） 大家可以自己配置需要的解析器 示例如下
    - 需要实现 `SerialDataParser<T>` 接口 的` public T parse(byte[] bytes)` 方法。解析为相应的对象
    - 不要忘记要加入到spring的IOC容器中，才能对数据进行处理哦
      ```java
      public class StringSerialDataParser implements SerialDataParser<String> {
          @Override
          public String parse(byte[] bytes) {
              return new String(bytes);
          }
      }
      ``` 
5. 配置数据处理器

   没有进行任何的默认配置 需要的请自行配置
    - 要实现`SerialDataProcessor<T>` 接口在 `public void processor(T t)`方法中对数据进行处理
    - 要将该处理器加入到spring的IOC容器中。 配置方法如下所示
    ```java
    @Component
    public class XXXProcessor implements SerialDataProcessor<String> {
      @Override
      public void processor(String s) {
          System.out.println(s);
      }
    }
    ```  
4. 启动

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
