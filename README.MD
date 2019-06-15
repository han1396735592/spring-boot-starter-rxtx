
spring boot java 对串口的读取的快速方法。
##使用方法

1. 引入依赖
```
<dependency>
    <groupId>cn.qqhxj.common</groupId>
     <artifactId>spring-boot-starter-rxtx</artifactId>
     <version>1.3-RELEASE</version>
</dependency>
```
2. 串口配置
```
serialport.baud-rate=115200
serialport.port-name=COM10
```
3. 串口数据解析器配置（可选）
   默认配置了  前后数据开始为‘{’， 数据结束为‘}’的数据解析器 ，该解析器只能解析为字符串
  大家可以自己配置需要的解析器 示例如下
  * 需要实现 `SerialDataParser<T>` 接口在` public T parse(byte[] bytes)` 解析为相应的对象
  * 不要忘记要加入到spring的IOC容器中，才能对数据进行处理哦
  ```java
  public class StringSerialDataParser implements SerialDataParser<String> {
      @Override
      public String parse(byte[] bytes) {
          return new String(bytes);
      }
  }
  ``` 
4. 配置数据处理器
   没有进行任何的默认配置
   需要的请自行配置
        * 要实现`SerialDataProcessor<T>` 接口在 `public void processor(T t)`方法中对数据进行处理
        * 要将该处理器加入到spring的IOC容器中。 
   配置方法如下所示 
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
    

"# spring-boot-starter-rxtx" 
