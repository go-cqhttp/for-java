# go-cqhttp/java

> 基于 go-cqhttp 和 java 的 qq 机器人

### 使用前

- 在 https://github.com/Mrs4s/go-cqhttp/releases 下载对应平台的可执行文件, 放到 go-cqhttp 目录中

- 运行 `go-cqhttp/下载的文件`, 根据提示填写 QQ 号和密码等信息, 参考文档 https://docs.go-cqhttp.org/guide/quick_start.html

- 根据文档将go-cqhttp的上报方式修改为Array

- clone并执行mvn clean install

- 创建你的springboot项目并引用依赖
    ```
    <dependency>
        <groupId>com.zhuangxv</groupId>
        <artifactId>bot-core</artifactId>
        <version>1.0.0</version>
    </dependency>
    ```
- 在启动类上加注解@EnableBot

### 配置

>在resources目录下新建application.yml,如果您熟悉spring,可根据自己需求自行编写配置.

```
bot:
  websocketUrl: #go-cqhttp配置中的正向websocket地址
  websocketPort: #go-cqhttp配置中的正向websocket端口号
  accessToken: #go-cqhttp配置中的access_token
```

### 开始使用

* 创建一个类并加入spring管理(可以在类上加@Service注解)
* 对应方法加上需要解析的类型即可监听对应事件，如下
    * @GroupMessageHandler 监听群消息
        * regex 匹配改正则消息时触发该事件
        * groupIds 只有当收到消息的群号为该参数指定内容时，触发该事件，默认为0即不限制
        * senderIds 只有当发言人为该参数指定id时，触发该事件，默认为0即不限制
        * isAt 是否被艾特，如果为true则被艾特的消息才会触发该事件，反之不会触发。
    * @FriendMessageHandler 监听私聊消息
        * regex 匹配改正则消息时触发该事件
        * senderIds 只有当发言人为该参数指定id时，触发该事件，默认为0即不限制
    * @TempMessageHandler 监听临时会话
        * regex 匹配改正则消息时触发该事件
        * groupIds 只有当临时会话从该参数指定群聊发起时，触发该事件，默认为0即不限制
        * senderIds 只有当发言人为该参数指定id时，触发该事件，默认为0即不限制
    * @GroupRecallHandler
        * groupIds
        * senderIds
    * 待补充。
* 方法支持的参数列表(你创建的方法中参数列表的类型允许下列中的任意一个,参数名随意,通过类型区分,不需要的可以不加.)
    * Group 如果是群消息，会注入群对应实例，否则注入null
    * GroupMessageEvent 如果是群消息,会注入对应消息事件, 否则注入null
    * String 消息内容
    * (Integer||int) 消息id
    * MessageChain 消息体
    * Member 如果是群消息，会注入发送人(群成员)对应实例，否则注入null
    * Friend 如果是私聊消息或临时会话,会注入发送人对应实例，否则注入null
    * RecallMessage 如果是撤回消息的事件，会注入撤回消息的对应实例 
    * ...文档待补充
* 方法支持的返回值列表
    * void 什么也不做
    * MessageChain 回复对应消息
* MessageChain
    * at 增加艾特指定qq
    * atAll 增加艾特全体成员
    * text 增加普通文本消息
    * image 增加自定义图片,参数支持url文本
    * reply 回复指定消息
    * record 增加语音,参数支持url文本
    * copy 复制一个MessageChain对象
* 各个组件可进行的操作
    * ...文档待补充