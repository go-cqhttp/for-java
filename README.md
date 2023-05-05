# go-cqhttp/for-java

> 基于 go-cqhttp 和 java 的 qq 机器人

## 使用前

- 在 https://github.com/Mrs4s/go-cqhttp/releases 下载对应平台的可执行文件, 放到 go-cqhttp 目录中

- 运行 `go-cqhttp/下载的文件`, 根据提示填写 QQ 号和密码等信息, 参考文档 https://docs.go-cqhttp.org/guide/quick_start.html

- 根据文档将go-cqhttp的上报方式修改为Array

- clone并执行mvn clean install

- 创建你的springboot项目并引用依赖
    ```xml
    <dependency>
        <groupId>com.zhuangxv</groupId>
        <artifactId>bot-core</artifactId>
        <version>1.0.0</version>
    </dependency>
    ```
- 在启动类上加注解@EnableBot

## 配置

>在resources目录下新建application.yml,如果您熟悉spring,可根据自己需求自行编写配置.

```yml
bot:
  type: #连接类型，可选：ws, ws-reverse,http(开发中)
  url: #go-cqhttp的地址,要附带协议(如ws://127.0.0.1:6700)
  accessToken: #go-cqhttp配置中的access_token
```

## 开始使用

* 创建一个SpringBoot项目
* 创建一个类并加入spring管理(可以在类上加@Service注解,注意配置扫描包路径)
* 创建一个方法，并在该方法上增加事件注解即可监听该事件(事件注解用法见下方)
  
## 事件注解用法
所有事件注解的参数都是限制该注解是否生效的依据，当全部满足时才会调用该注解所对应的方法，不同事件可注入的对象不同，具体见下方。

--- 

### @GroupMessageHandler
> 收到群消息时执行该方法

注解参数列表

* regex 正则表达式，该值不为默认值时，将验证消息是否匹配
* groupIds 验证收到消息的群号是否为当前值的内容，默认为0即不限制
* excludeGroupIds 验证收到消息的群号是否非当前值的内容，默认为0即不限制
* senderIds 验证收到消息的人是否为当前值的内容，默认为0即不限制
* excludeSenderIds 验证收到消息的人是否非当前值的内容，默认为0即不限制
* isAt 验证是否被艾特，默认为false

可注入到方法中的属性

* Group 该消息所对应群的实例
* Member 该消息发送者所对应的群成员实例
* MessageChain 该消息的消息链形式
* String 该消息的字符串形式  
* Integer/int 该消息的id

---

### @FriendMessageHandler
> 收到好友私聊消息时执行该方法

注解参数列表

* regex 正则表达式，该值不为默认值时，将验证消息是否匹配
* senderIds 验证收到消息的人是否为当前值的内容，默认为0即不限制
* excludeSenderIds 验证收到消息的人是否非当前值的内容，默认为0即不限制

可注入到方法中的属性

* Friend 该消息发送者所对应的好友实例
* MessageChain 该消息的消息链形式
* String 该消息的字符串形式
* Integer/int 该消息的id

---

### @TempMessageHandler
> 收到临时会话时执行该方法

注解参数列表

* regex 正则表达式，该值不为默认值时，将验证消息是否匹配
* senderIds 验证收到消息的人是否为当前值的内容，默认为0即不限制
* excludeSenderIds 验证收到消息的人是否非当前值的内容，默认为0即不限制

可注入到方法中的属性

* TempFriend 该消息发送者所对应的好友实例
* MessageChain 该消息的消息链形式
* String 该消息的字符串形式
* Integer/int 该消息的id

---

### @GroupRecallHandler
> 有群消息撤回时执行该方法

注解参数列表

* groupIds 验证撤回消息的群号是否为当前值的内容，默认为0即不限制
* excludeGroupIds 验证撤回消息的群号是否非当前值的内容，默认为0即不限制  
* senderIds 验证撤回消息的操作人是否为当前值的内容，默认为0即不限制
* excludeSenderIds 验证撤回消息的操作人是否非当前值的内容，默认为0即不限制

可注入到方法中的属性

* Group 被撤回的消息所在的群实例
* MessageChain 被撤回的消息的消息链形式
* String 被撤回的消息的字符串形式
* Integer/int 被撤回的消息id
* RecallMessage 包含撤回该消息的操作人以及被撤回消息的发送人id

---

### @GroupUserAddHandler
> 有用户入群时执行该方法

注解参数列表

* groupIds 验证撤回消息的群号是否为当前值的内容，默认为0即不限制
* excludeGroupIds 验证撤回消息的群号是否非当前值的内容，默认为0即不限制
* senderIds 验证撤回消息的操作人是否为当前值的内容，默认为0即不限制
* excludeSenderIds 验证撤回消息的操作人是否非当前值的内容，默认为0即不限制

可注入到方法中的属性

* Group 被撤回的消息所在的群实例
* UserAddMessage 包含操作管理员以及入群用户的id**
