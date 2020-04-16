# NRPC 简介 
NRPC(new RPC)主要分为两大部分 一部分是基于Netty的RPC 框架，另一部分是使用自定义协议的服务器。

### NRPC Defined Server 自定义协议服务器
此功能通过解析自定义的协议扮演一个自定义协议服务器。Nginx 可以解析HTTP协议所以称之为HTTP服务器。Redis中的Redis服务器也解析了自定义的Redis协议。同理MySQL,WebSocket也是如此。

使用Netty可以自定义编码协议，实现自己的特定协议的服务器。在NRPC Defined Server 中通过配置端口和相应端口使用的协议来启动不同的服务。

这些协议用于物联网中，设备通过 Socket 发送的字节数据。

其中实现的功能涉及 配置 解决TPC粘包、拆包。维护客户端的长连接、往指定客户端发送消息等。

### NRPC RPC 
RPC 部分就是自己实现的RPC框架。

RPC 部分涉及服务端注册zookeeper ，维持心跳。使用的HTTP/2 协议。使用kyro进行序列化。负载均衡，限流，熔断，链路追踪等。

##### RPC TODO 
- [ ] 服务端注册zookeeper
- [ ] HTTP/2 协议
- [ ] kyro进行序列化
- [ ] 限流
- [ ] 负载均衡
- [ ] 熔断
- [ ] 链路追踪
