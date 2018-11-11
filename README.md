# Fabric Client Rest [![llong](https://img.shields.io/badge/made%20by-%E7%81%B5%E9%BE%99-brightgreen.svg)](http://www.cnblogs.com/aberic/)

## 1. Java-SDK简介
Java-SDK是外部应用程序与Hyperledger Fabric的交互通道，帮助Java应用程序更好的管理Fabric通道和链码的生命周期，提供了链码管理、查询通道上的区块和交易数据的接口，及通道发生事件的监控。

## 2. Java-SDK代码分析
官方的fabric-sdk-java下载地址为https://github.com/hyperledger/fabric-sdk-java ，目前版本为Java SDK for Hyperledger Fabric 1.3，下载源码后，使用IntelliJ IDEA导入工程，显示结构如下：
<div align=center>
   <img width="300" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/fabric-sdk-java源码.jpg"/ alt="fabric-sdk-java源码">
</div>
源码包括两个包：org.hyperledger.fabric.sdk和org.hyperledger.fabric_ca.sdk。

* org.hyperledger.fabric.sdk：提供与区块链交互的接口，包括创建通道、加入通道、查询通道等通道接口；安装链码、案例化链码、发起交易、查询交易等链码接口；根据编号查询区块、根据Hash值查询区块等区块接口。
* org.hyperledger.fabric_ca.sdk：提供与fabric ca的交互的接口，包括登记、注册、销毁证书等接口。
外部应用程序要通过fabric-java-sdk接口调用Fabric网络数据，只需在pom.xml文件中引入如下代码，即可调用：

<dependencies>
        <!-- https://mvnrepository.com/artifact/org.hyperledger.fabric-sdk-java/fabric-sdk-java -->
        <dependency>
            <groupId>org.hyperledger.fabric-sdk-java</groupId>
            <artifactId>fabric-sdk-java</artifactId>
            <version>1.2.0-SNAPSHOT</version>
        </dependency>
</dependencies>

外部应用程序访问区块链是通过实例化HFClient类，调用类中的接口。访问Fabric ca是通过实例化HFCAClient类，调用类中的接口。

### 2.1 fabric.sdk主要类关系图
类图如下：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/fabric主要类关系.jpg"/ alt="fabric主要类关系">
</div>
类说明：

类名	说明
HFClient	Hyperledger fabric客户端访问类，通过实例化HFClient类，调用其中的接口与区块链交互。
Orderer	Orderer类实现客户端部署、调用和查询排序（Orderer）服务。
Peer	Peer类实现客户端部署、调用和查询节点（Peer）。
Channel	Channel类实现客户端与通道交互。
User	User是用户接口
InstallProposalRequest	InstallProposalRequest是智能合约安装提案请求类。
InstantiateProposalRequest	InstantiateProposalRequest是智能合约实例化提案请求类。
TransactionProposalRequest	TransactionProposalRequest是智能合约交易提案请求类。
QueryByChaincodeRequest	QueryByChaincodeRequest是智能合约查询提案请求类。

### 2.2	fabric_ca.sdk主要类关系图
类图：
<div align=center>
   <img width="400" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/fabric_ca主要类.jpg"/ alt="fabric_ca主要类">
</div>
类说明：
主要方法	说明
register	register是注册用户身份方法。
enroll	enroll是登记用户身份方法。
reenroll	reenroll是重新登记用户身份方法。
revoke	revoke是注销已签发的用户证书方法。

## 3 Java-SDK优化
Java-SDK直接调用对于初学者有很大的难度，为了最方便外部应用程序的调用，本节在官方Java-SDK的基础上进行优化和封装，提供简洁的、跨开发语言的调用方式。

### 3.1	编码思路
封装官方Java-SDK代码，需要达到两个目的：

#### 1） 提供RESTful风格的调用方法，以http方式调用解决跨开发语言问题。

#### 2) 提供参数在线配置界面，包括排序（Orderer）IP地址、节点（peer）IP地址、智能合约（smart contract）所在目录等参数配置。

封装的中间层工程取名为fabricClientRest，以下介绍工程的编码环境的搭建过程、开发类之间的关系及接口说明。

### 3.2	编码环境搭建
#### 1) 创建工程

##### 步骤1：运行IntelliJ IDEA工具，点击File->New->Project...，新建工程，在New Project界面中选择“Maven”后，点击“Next”，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤1.jpg"/ alt="编码环境搭建步骤1">
</div>

##### 步骤2：设置GroupId为“com.winyeahs.fabric”,ArtifactId为“fabric”,点击“Next”，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤2.jpg"/ alt="编码环境搭建步骤2">
</div>

##### 步骤3：设置Project name为“fabricClientRest”，Project locatiion为“[目录]\fabricClientRest”,点击“Finish” ，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤3.jpg"/ alt="编码环境搭建步骤3">
</div>

##### 步骤4：创建的工程为总的工程，不需要编写代码，逻辑代码在“sdkInterface” 模块和“clientRest” 模块中编写；选择 “src”目录，右击后，在弹出的菜单中选择“Delete...”,删除“src”目录，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤4.jpg"/ alt="编码环境搭建步骤4">
</div>

#### 2) 创建sdkInterface模块
##### 步骤5：在创建的工程fabricClientRest中右击，在显示的菜单中选择“New->Module”，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤5.jpg"/ alt="编码环境搭建步骤5">
</div>

##### 步骤6：在New Module界面中，选择“Maven”，其它默认设置，点击“Next”，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤6.jpg"/ alt="编码环境搭建步骤6">
</div>

##### 步骤7：设置GroupId为“com.winyeahs.fabric.sdkinterface”，Artifact为“sdkinterface”,点击“Nest”，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤7.jpg"/ alt="编码环境搭建步骤7">
</div>

##### 步骤8：设置Content root和Module file location，点击“Finish”，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤8.jpg"/ alt="编码环境搭建步骤8">
</div>

#### 3) 生成sdkInterface模块所需类
##### 步骤9：在工程界面中右击com.winyeahs.fabric.sdkinterface包目录，在弹出的菜单中选择“New->Java Class”，依次创建SdkInterfaceBase、SdkInterfaceOrg、SdkInterfaceOrderer、SdkInterfacePeer、SdkInterfaceUser、SdkInterfaceChannel、SdkInterfaceChaincode等类，类中的具体代码查看源码文件，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤9.jpg"/ alt="编码环境搭建步骤9">
</div>

#### 4) 创建clientRest模块
##### 步骤10：在创建的工程fabricClientRest中右击，在显示的菜单中选择“New->Module”，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤10.jpg"/ alt="编码环境搭建步骤10">
</div>

##### 步骤11：在New Module界面中，选择“spring initializr”，其它默认设置，点击“Next”，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤11.jpg"/ alt="编码环境搭建步骤11">
</div>

##### 步骤12：设置Group为“com.winyeahs.fabric.clientrest”，Artifact为“clientrest”,点击“Nest”，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤12.jpg"/ alt="编码环境搭建步骤12">
</div>

##### 步骤13：选择“web->web”,点击“Nest”，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤13.jpg"/ alt="编码环境搭建步骤13">
</div>

##### 步骤14：设置Content root和Module file location，点击“Finish”，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤14.jpg"/ alt="编码环境搭建步骤14">
</div>

#### 5) 生成clientRest模块所需类

##### 步骤15：在工程界面中右击com.winyeahs.fabric.clientrest包目录，在弹出的菜单中选择“New->Package”，依次创建rest、sdk、service包，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤15.jpg"/ alt="编码环境搭建步骤15">
</div>

##### 步骤16：创建RESTful风格访问的所需类，代码结构分为控制器（controller）、服务（service）和接口(sdk)三部分。

* 控件器（controller）：分为智能合约控制器（ChainCodeController）和区块控制器（ChainCodeController）,文件创建在rest包目录下面。

* 服务（service）：分为智能合约服务（ChainCodeService）和区块服务（ChainCodeService）,接口文件在service包目录下面创建；实现文件在service\impl目录下面创建。

* 接口(sdk)：一端以单例方式提供给服务层调用，另一端调用sdkInterface工程中的接口。

界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/编码环境搭建步骤16.jpg"/ alt="编码环境搭建步骤16">
</div>

### 3.3	编码类图
Java-SDK优化工程fabricClientRest分clientrest模块和sdkinterface模块。clientrest模块实现RESTful风格调用的接口；sdkinterface模块实现与官方fabric-sdk-java接口进行交互。

#### 1) clientrest模块
clientrest模块包括控制器（controller）、服务（service）和接口(sdk)，配合实现RESTful接口和调用sdkinterface模块接口功能。

* 控制器（controller）：分为智能合约控制器类（ChainCodeController）和区块控制器类（ChainCodeController），第三方应用系统通过调用控制器提供的接口实现具体业务；

* 服务（service）：分为智能合约服务接口（ChainCodeService）和区块服务接口（ChainCodeService），实现类分别为ChainCodeServiceImpl和ChainBlockServiceImpl；

* 接口(sdk)：SdkManager类以单例方式提供给服务（service）调用；SdkManager类实例化时，从数据库中读取区块链的各种配置，设置调用环境参数；功能通过实例化sdkinterface模块的SdkInterfaceOrg类，做为系统调用的统一入口。

#### 2) sdkinterface模块
sdkinterface模块实现官方fabric-sdk-java接口的优化，让使用者更清晰、最方便的调用。创建了SdkInterfaceOrg、SdkInterfaceChannel、SdkInterfaceOrderer、SdkInterfacePeer、SdkInterfaceChaincode和SdkInterfaceUser等类，都继承于SdkInterfaceBase类。

* SdkInterfaceOrg类：组织类，对应Faric网络中的组织，类中包含组织名称、组织ID、排序（orderer）服务集合、节点（Peer）集合、通道和智能合约等变量；统一由该类提供接口给clientRest模块调用；

* SdkInterfaceChannel类：通道类，负责处理通道相关接口，包括查询当前频道的链信息、根据交易Id查询区块数据、根据区块高度查询区块数据、根据交易Id查询区块数据；

* SdkInterfaceOrderer类：排序类，负责记录排序服务名称、排序服务地址等信息；

* SdkInterfacePeer类：节点类，负责记录节点名称、节点地址、节点事件域名和节点事件监听地址等信息；

* SdkInterfaceChaincode类：智能合约类，负责处理智能合约记录智能合约名称、智能合约版本号、智能合约ID等信息，及实现安装链码、实例化链码、升级链码、执行链码和查询链码等接口；

* SdkInterfaceUser类：用户类，实现官方fabric-sdk-java中的User接口，负责记录用户名称、用户规则、用户帐户、所属联盟等信息。
类关系图如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/fabricClientRest工程类关系.jpg"/ alt="fabricClientRest工程类关系">
</div>

### 3.4	REST接口说明
第三方系统调用的REST接口分为智能合约接口和区块信息接口。

#### 1) 智能合约接口：智能合约接口有安装智能合约、实例化智能合约、升级智能合约、执行智能合约、查询智能合约。
* 安装智能合约
接口调用请求说明
http请求方式	POST
http请求地址	http://{域名}/chaincode/install
参数说明
参数	是否必须	说明
-	-	-
返回说明
正常情况下，返回下述JSON数据包：

参数	说明
	
* 实例化智能合约
接口调用请求说明
http请求方式	POST
http请求地址	http://{域名}/chaincode/instantiate
参数说明
参数	是否必须	说明
array	是	["a","200","b","400"]表示：a初始值200，b初始值400
post格式：{"array":["a","200","b","400"]}
返回说明
正常情况下，返回下述JSON数据包：

参数	说明
	
* 升级智能合约
接口调用请求说明
http请求方式	POST
http请求地址	http://{域名}/chaincode/upgrade
参数说明
参数	是否必须	说明
array	是	["a","200","b","400"]表示：a初始值200，b初始值400
post格式：{"array":["a","200","b","400"]}
返回说明
正常情况下，返回下述JSON数据包：

参数	说明
	
* 执行智能合约
接口调用请求说明
http请求方式	POST
http请求地址	http://{域名}/chaincode/invoke
参数说明
参数	是否必须	说明
fcn	是	函数名
array	是	["b", "a", "5"]表示：b向a转移值5
post格式：{"fcn":"invoke","array":["b", "a", "5"]}
返回说明
正常情况下，返回下述JSON数据包：

参数	说明
	
* 查询智能合约
接口调用请求说明
http请求方式	POST
http请求地址	http://{域名}/chaincode/query
参数说明
参数	是否必须	说明
fcn	是	函数名
array	是	["a"]表示：查询a值
post格式：{"fcn":"invoke","array":["a"]}
返回说明
正常情况下，返回下述JSON数据包：

参数	说明
	
#### 2) 区块信息接口：区块信息接口有根据交易Id查询区块数据、根据哈希值查询区块数据、根据区块高度查询区块数据、查询当前区块信息。
* 根据交易Id查询区块数据：
接口调用请求说明
http请求方式	POST
http请求地址	http://{域名}/chainblock/queryBlockByTransactionID
参数说明
参数	是否必须	说明
txId	是	交易ID
post格式：{"txId":"f30522c5db02341fa06cb0c1d662b2184d75eba1df7327af8c6327f1a11c4092"}
返回说明
正常情况下，返回下述JSON数据包：

参数	说明
	
* 根据哈希值查询区块数据：
接口调用请求说明
http请求方式	POST
http请求地址	http://{域名}/chainblock/queryBlockByHash
参数说明
参数	是否必须	说明
hash	是	Hash值
post格式：{"hash":"ff0ceb01a804e98fae405ea075748fa149899e85bc0f03f939c9b6e7f6b89668"}
返回说明
正常情况下，返回下述JSON数据包：

参数	说明
	
* 根据区块高度查询区块数据：
接口调用请求说明
http请求方式	POST
http请求地址	http://{域名}/chainblock/queryBlockByNumber
参数说明
参数	是否必须	说明
blockNumber	是	区块高度
post格式：{"blockNumber":"1"}
返回说明
正常情况下，返回下述JSON数据包：

参数	说明
	
* 查询当前区块信息：
接口调用请求说明
http请求方式	POST
http请求地址	http://{域名}/chainblock/queryBlockchainInfo
参数说明
参数	是否必须	说明
-	-	-
post格式：无
返回说明
正常情况下，返回下述JSON数据包：

参数	说明
	
## 4 生产环境调用介绍
生产环境中每个节点（Peer）对于一台服务器，每个节点（Peer）可能属于不同的企业，所以fabricClientRest spring boot项目在每个节点（Peer）中都部署一套，提供企业中第三方系统调用。
以下将介绍fabricClientRest spring boot项目的打包、在linux下java安装及在kafka生产环境中运行客户端调试。

### 4.1 项目打包
FabricClientRest工程中包括clientRest模块和sdkInterface模块，FabricClientRest的artifactId为fabric,只要在maven projects中双击package即可，生成的spring boot项目的jar文件在clientRest\target这个目录下。

##### 步骤1：打开FabricClientRest工程,界面显示如下：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/生产环境调用介绍步骤1.jpg"/ alt="生产环境调用介绍步骤1">
</div>

##### 步骤2：单击界面中右边的“Maven project”项目，显示界面如下：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/生产环境调用介绍步骤2.jpg"/ alt="生产环境调用介绍步骤2">
</div>

##### 步骤3：在Maven Projects中双击“package”菜单，工程自动生成jar文件，生成完的界面如下：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/生产环境调用介绍步骤3.jpg"/ alt="生产环境调用介绍步骤3">
</div>

##### 步骤4：把clientRest\target目录下生成clientrest-1.0-SNAPSHOT.jar包拷贝到前一章的kafka生产环境部署的peer0.org1.example.com对应的服务器（ip:192.168.35.7）的/usr/local/clientrest某个目录下，生成jar的界面如下：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/生产环境调用介绍步骤4.jpg"/ alt="生产环境调用介绍步骤4">
</div>

### 4.2 java环境安装
##### 步骤5：下载JDK8，访问地址：http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html，下载jdk-8u191-linux-x64.tar.gz文件，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/生产环境调用介绍步骤5.jpg"/ alt="生产环境调用介绍步骤5">
</div>

##### 步骤6：拷贝jdk-8u191-linux-x64.tar.gz文件到/usr/local/目录下，执行解压命令。
\# cd /usr/local/
\# tar zxvf jdk-8u191-linux-x64.tar.gz

##### 步骤7：设置环境变量
\# vi /etc/profile
添加如下内容
export JAVA_HOME=/usr/local/jdk1.8.0_191
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$PATH:$JAVA_HOME/bin

##### 步骤8：环境变量设置立即生效
\# source /etc/profile

##### 步骤9：查看JDK版本
\# java -version

### 4.3 生产环境部署
##### 步骤10：根据第十一章 Fabric kafka生产环境部署启动Fabric网络，kafka运行验证由java-sdk客户端处理。

##### 步骤11：运行clientrest的spring boot系统。
\# cd /usr/local/clientrest
\# netstat -lanp|grep 8080
\# kill -9 XXXX
\# nohup java -jar clientrest-1.0-SNAPSHOT.jar >springboot.log 2>&1 &
\#  tail -f springboot.log

##### 步骤12：拷贝整个crypto-config目录和Fabric-sdk-soapui-project.xml到/usr/local/clientrest目录中。

##### 步骤13：验证是否正常运行，通过浏览器访问http://192.168.235.7：8080，出现如下界面表示系统已正确部署。
 
##### 步骤14：安装SoapUI测试工具（在光盘中有安装软件），安装完后运行的界面如下：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/生产环境调用介绍步骤14.jpg"/ alt="生产环境调用介绍步骤14">
</div>

##### 步骤15：点击File->import Project，在出现的界面中加载Fabric-sdk-soapui-project.xml（在光盘中有该文件），界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/生产环境调用介绍步骤15.jpg"/ alt="生产环境调用介绍步骤15">
</div>

### 4.4 客户端验证
在SoapUI工具上通过调用FabricClientRest提供的REST接口验证各功能，界面如下所示：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/SoapUI工具.jpg"/ alt="SoapUI工具">
</div>

##### 1) 安装链码
打开安装链码测试项，以POST请求方式调用http://192.168.235.7:8080/chaincode/install接口，如果调用成功，以json方式返回结果，返回字符串及界面如下所示：
调用返回：
{
   "result": "OK",
   "txid": "7cc546ba6eb5f3af5d4bf2a4191616f45b2c99f4c966c6b342b22a05f0a635eb",
   "status": 200
}
界面：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/安装链码.jpg"/ alt="安装链码">
</div>

##### 2) 实例化链码
打开安装链码测试项，以POST请求方式调用http://192.168.235.7:8080/chaincode/instantiate接口，如果调用成功，以json方式返回结果，请求参数、返回字符串及界面如下所示：
请求参数：
{"array":["a","200","b","400"]}
返回字符串：
{
   "result": "",
   "txid": "67031002ca3ed1e2f2f30824707313d0143c20199af024c06a868b726f160965",
   "status": 200
}
界面：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/实例化链码.jpg"/ alt="实例化链码">
</div>

##### 3) 执行链码
打开安装链码测试项，以POST请求方式调用http://192.168.235.7:8080/chaincode/invoke接口，如果调用成功，以json方式返回结果，请求参数、返回字符串及界面如下所示：
请求参数：
{"fcn":"invoke","array":["b", "a", "5"]}
返回字符串：
{
   "result": "",
   "txid": "5412bf55447701c58753dc90416e6ddcafc3c280fc5585b7316fbd97e9f5d3fb",
   "status": 200
}
界面：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/执行链码.jpg"/ alt="执行链码">
</div>

##### 4) 查询链码
打开安装链码测试项，以POST请求方式调用http://192.168.235.7:8080/chaincode/query接口，如果调用成功，以json方式返回结果，请求参数、返回字符串及界面如下所示：
请求参数：
{"fcn":"query","array":["b"]}
返回字符串：
{
   "result": "400",
   "txid": "c4963ae1d92f3a3bcc08647141ca56054db134fe8b22cd5f0d36cd119b9564de",
   "status": 200
}
界面：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/查询链码.jpg"/ alt="查询链码">
</div>

##### 5) 查询当前区块
打开安装链码测试项，以POST请求方式调用http://192.168.235.7:8080/chainblock/ queryBlockchainInfo接口，如果调用成功，以json方式返回结果，返回字符串及界面如下所示：
返回字符串：
{
   "data": {
      "previousBlockHash": "5f67bcaeb2c7c91d4d6f3fe45f0319b838e9c513b46e8ea4bbd42d93a267b160",
      "currentBlockHash": "f222ed19805574813c4a4148efc3e9352d6e498e8048719bb379fd65f3fe2561",
      "height": 2
   },
   "status": 200
}
界面：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/查询当前区块.jpg"/ alt="查询当前区块">
</div>

##### 6) 根据高度查询区块
打开安装链码测试项，以POST请求方式调用http://192.168.235.7:8080/chainblock/ queryBlockByNumber接口，如果调用成功，以json方式返回结果，请求参数、返回字符串及界面如下所示：
请求参数：
{"blockNumber":"1"}
返回字符串：
{
   "data":    {
      "dataHash": "94bee5df076c55b444ed19edc8e47fa924db3341c3b7beb4cb4785b584bf5ce1",
      "blockNumber": 1,
      "calculatedBlockHash": "f222ed19805574813c4a4148efc3e9352d6e498e8048719bb379fd65f3fe2561",
      "envelopeCount": 1,
      "envelopes": [      {
         "transactionEnvelopeInfo":          {
            "transactionActionInfoArray": [            {
               "chaincodeInputArgsCount": 6,
               "endorserInfoArray": [               {
                  "mspId": "Org1MSP",
                  "signature": "3044022007677a5e43c566a9667b8f6449193155eee653ee45270e4246938e2155fcdad1022046223d9dca07872f8421427039adde582bc9d8b27b763a79eccd7e3cd9d59c34",
                  "id": "-----BEGIN CERTIFICATE-----\nMIICKDCCAc+gAwIBAgIRALGx/vw0QOlTBNy0sDFLGkAwCgYIKoZIzj0EAwIwczEL\nMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFjAUBgNVBAcTDVNhbiBG\ncmFuY2lzY28xGTAXBgNVBAoTEG9yZzEuZXhhbXBsZS5jb20xHDAaBgNVBAMTE2Nh\nLm9yZzEuZXhhbXBsZS5jb20wHhcNMTgwOTI0MTM1ODM5WhcNMjgwOTIxMTM1ODM5\nWjBqMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMN\nU2FuIEZyYW5jaXNjbzENMAsGA1UECxMEcGVlcjEfMB0GA1UEAxMWcGVlcjAub3Jn\nMS5leGFtcGxlLmNvbTBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IABNqgityE093u\n0GEJMj10rjp/VV//BctP3YabP08W/zLcWoCJNiAa3Zu++yiKaIKxhIRmeiAZ9rLF\nnrHP/nhBZ8qjTTBLMA4GA1UdDwEB/wQEAwIHgDAMBgNVHRMBAf8EAjAAMCsGA1Ud\nIwQkMCKAIL71mqycUvRcWvL+HPSBZFM0neNouwtaALLOlAO3i0yIMAoGCCqGSM49\nBAMCA0cAMEQCICQa2yat4HdN55N2n8AsJIyKexeMC33y6WOrh2U2z50lAiB06y8W\nXsLSnCpwwfKIIxagUvMxTb9Inre8upwtFVUwmA==\n-----END CERTIFICATE-----\n"
               }],
               "payload": "",
               "argArray":                [
                  "deploy",
                  "mychannel",
                  "escc",
                  "vscc"
               ],
               "endorsementsCount": 1,
               "rwsetInfo":                {
                  "nsRwsetInfoArray":                   [
                                          {
                        "writeSet": [                        {
                           "writeSetIndex": 0,
                           "namespace": "lscc",
                           "value": "",
                           "key": "mycc"
                        }],
                        "readSet": [                        {
                           "readSetIndex": 0,
                           "readVersionTxNum": 0,
                           "readVersionBlockNum": 0,
                           "namespace": "lscc",
                           "version": "[0 : 0]",
                           "key": "mycc"
                        }]
                     },
                                          {
                        "writeSet":                         [
                                                      {
                              "writeSetIndex": 0,
                              "namespace": "mycc",
                              "value": "200",
                              "key": "a"
                           },
                                                      {
                              "writeSetIndex": 1,
                              "namespace": "mycc",
                              "value": "400",
                              "key": "b"
                           }
                        ],
                        "readSet": []
                     }
                  ],
                  "nsRWsetCount": 2
               },
               "responseStatus": 200,
               "responseMessageString": "",
               "status": 200
            }],
            "txCount": 1,
            "isValid": true,
            "validationCode": 0
         },
         "createId": "-----BEGIN CERTIFICATE-----\nMIICKzCCAdGgAwIBAgIRAKIquOLmST9u5iLYTmTbXxgwCgYIKoZIzj0EAwIwczEL\nMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFjAUBgNVBAcTDVNhbiBG\ncmFuY2lzY28xGTAXBgNVBAoTEG9yZzEuZXhhbXBsZS5jb20xHDAaBgNVBAMTE2Nh\nLm9yZzEuZXhhbXBsZS5jb20wHhcNMTgwOTI0MTM1ODM5WhcNMjgwOTIxMTM1ODM5\nWjBsMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMN\nU2FuIEZyYW5jaXNjbzEPMA0GA1UECxMGY2xpZW50MR8wHQYDVQQDDBZBZG1pbkBv\ncmcxLmV4YW1wbGUuY29tMFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEaZh8qgfm\ny1v9CLdWHuPYMc/lNyGW2TPS1ARdi+CCoO7o6p5BFu/1KOrL7wHFyD0Oc7VOC/uA\nWzPfcnLsyyqLnKNNMEswDgYDVR0PAQH/BAQDAgeAMAwGA1UdEwEB/wQCMAAwKwYD\nVR0jBCQwIoAgvvWarJxS9Fxa8v4c9IFkUzSd42i7C1oAss6UA7eLTIgwCgYIKoZI\nzj0EAwIDSAAwRQIhALR8vjnvpiPqjhVjo+CcFIhqU3W69eGSP39EdIMFHg+fAiAM\njUCAQ7Nk+2LrR62w14/llpT5rm1p8o4WVZ/8Lqq5Bw==\n-----END CERTIFICATE-----\n",
         "isValid": true,
         "validationCode": 0,
         "type": "TRANSACTION_ENVELOPE",
         "nonce": "64689a2947187379a6e57691a150b49121fe4a8a886061fa",
         "channelId": "mychannel",
         "transactionID": "688b65f86ce280f165979f31d94c4fc7caa28a6a33650e337f39bacaedd9d3b3",
         "createMSPID": "Org1MSP",
         "timestamp": "2018/11/08 21:58:30"
      }],
      "previousHashID": "5f67bcaeb2c7c91d4d6f3fe45f0319b838e9c513b46e8ea4bbd42d93a267b160"
   },
   "status": 200
}
界面：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/根据高度查询区块.jpg"/ alt="根据高度查询区块">
</div>

##### 7) 根据交易ID查询区块
打开安装链码测试项，以POST请求方式调用http://192.168.235.7:8080/chainblock/queryBlockByTransactionID接口，如果调用成功，以json方式返回结果，请求参数、返回字符串及界面如下所示：
请求参数：
{"txId":"5412bf55447701c58753dc90416e6ddcafc3c280fc5585b7316fbd97e9f5d3fb"}
返回字符串：
{
   "data":    {
      "dataHash": "2a1564a54721489baf18388e4d7429259e39c48fb63eedc4520286f3966da845",
      "blockNumber": 2,
      "calculatedBlockHash": "b81a92fc5f0427e17cecf461efcf05733b084d691ccf6b56c51f9a94bffa8e27",
      "envelopeCount": 1,
      "envelopes": [      {
         "transactionEnvelopeInfo":          {
            "transactionActionInfoArray": [            {
               "chaincodeInputArgsCount": 4,
               "endorserInfoArray": [               {
                  "mspId": "Org1MSP",
                  "signature": "304402201640b5d2507e5d12342067643d6626b61804f53be93b11b81eef1a40269d08c802203d2325ad76b273d664a1f81574d0338d7b1d7a8c6fd0e7007ed9d85cdfc271ce",
                  "id": "-----BEGIN CERTIFICATE-----\nMIICKDCCAc+gAwIBAgIRALGx/vw0QOlTBNy0sDFLGkAwCgYIKoZIzj0EAwIwczEL\nMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFjAUBgNVBAcTDVNhbiBG\ncmFuY2lzY28xGTAXBgNVBAoTEG9yZzEuZXhhbXBsZS5jb20xHDAaBgNVBAMTE2Nh\nLm9yZzEuZXhhbXBsZS5jb20wHhcNMTgwOTI0MTM1ODM5WhcNMjgwOTIxMTM1ODM5\nWjBqMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMN\nU2FuIEZyYW5jaXNjbzENMAsGA1UECxMEcGVlcjEfMB0GA1UEAxMWcGVlcjAub3Jn\nMS5leGFtcGxlLmNvbTBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IABNqgityE093u\n0GEJMj10rjp/VV//BctP3YabP08W/zLcWoCJNiAa3Zu++yiKaIKxhIRmeiAZ9rLF\nnrHP/nhBZ8qjTTBLMA4GA1UdDwEB/wQEAwIHgDAMBgNVHRMBAf8EAjAAMCsGA1Ud\nIwQkMCKAIL71mqycUvRcWvL+HPSBZFM0neNouwtaALLOlAO3i0yIMAoGCCqGSM49\nBAMCA0cAMEQCICQa2yat4HdN55N2n8AsJIyKexeMC33y6WOrh2U2z50lAiB06y8W\nXsLSnCpwwfKIIxagUvMxTb9Inre8upwtFVUwmA==\n-----END CERTIFICATE-----\n"
               }],
               "payload": "",
               "argArray":                [
                  "invoke",
                  "b",
                  "a",
                  "5"
               ],
               "endorsementsCount": 1,
               "rwsetInfo":                {
                  "nsRwsetInfoArray":                   [
                                          {
                        "writeSet": [],
                        "readSet": [                        {
                           "readSetIndex": 0,
                           "readVersionTxNum": 0,
                           "readVersionBlockNum": 1,
                           "namespace": "lscc",
                           "version": "[1 : 0]",
                           "key": "mycc"
                        }]
                     },
                                          {
                        "writeSet":                         [
                                                      {
                              "writeSetIndex": 0,
                              "namespace": "mycc",
                              "value": "205",
                              "key": "a"
                           },
                                                      {
                              "writeSetIndex": 1,
                              "namespace": "mycc",
                              "value": "395",
                              "key": "b"
                           }
                        ],
                        "readSet":                         [
                                                      {
                              "readSetIndex": 0,
                              "readVersionTxNum": 0,
                              "readVersionBlockNum": 1,
                              "namespace": "mycc",
                              "version": "[1 : 0]",
                              "key": "a"
                           },
                                                      {
                              "readSetIndex": 1,
                              "readVersionTxNum": 0,
                              "readVersionBlockNum": 1,
                              "namespace": "mycc",
                              "version": "[1 : 0]",
                              "key": "b"
                           }
                        ]
                     }
                  ],
                  "nsRWsetCount": 2
               },
               "responseStatus": 200,
               "responseMessageString": "",
               "status": 200
            }],
            "txCount": 1,
            "isValid": true,
            "validationCode": 0
         },
         "createId": "-----BEGIN CERTIFICATE-----\nMIICKzCCAdGgAwIBAgIRAKIquOLmST9u5iLYTmTbXxgwCgYIKoZIzj0EAwIwczEL\nMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFjAUBgNVBAcTDVNhbiBG\ncmFuY2lzY28xGTAXBgNVBAoTEG9yZzEuZXhhbXBsZS5jb20xHDAaBgNVBAMTE2Nh\nLm9yZzEuZXhhbXBsZS5jb20wHhcNMTgwOTI0MTM1ODM5WhcNMjgwOTIxMTM1ODM5\nWjBsMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMN\nU2FuIEZyYW5jaXNjbzEPMA0GA1UECxMGY2xpZW50MR8wHQYDVQQDDBZBZG1pbkBv\ncmcxLmV4YW1wbGUuY29tMFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEaZh8qgfm\ny1v9CLdWHuPYMc/lNyGW2TPS1ARdi+CCoO7o6p5BFu/1KOrL7wHFyD0Oc7VOC/uA\nWzPfcnLsyyqLnKNNMEswDgYDVR0PAQH/BAQDAgeAMAwGA1UdEwEB/wQCMAAwKwYD\nVR0jBCQwIoAgvvWarJxS9Fxa8v4c9IFkUzSd42i7C1oAss6UA7eLTIgwCgYIKoZI\nzj0EAwIDSAAwRQIhALR8vjnvpiPqjhVjo+CcFIhqU3W69eGSP39EdIMFHg+fAiAM\njUCAQ7Nk+2LrR62w14/llpT5rm1p8o4WVZ/8Lqq5Bw==\n-----END CERTIFICATE-----\n",
         "isValid": true,
         "validationCode": 0,
         "type": "TRANSACTION_ENVELOPE",
         "nonce": "1201de624aa7736798d800795feefee3162e9d62f42e284a",
         "channelId": "mychannel",
         "transactionID": "5412bf55447701c58753dc90416e6ddcafc3c280fc5585b7316fbd97e9f5d3fb",
         "createMSPID": "Org1MSP",
         "timestamp": "2018/11/08 22:07:16"
      }],
      "previousHashID": "f222ed19805574813c4a4148efc3e9352d6e498e8048719bb379fd65f3fe2561"
   },
   "status": 200
}
界面：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/根据交易ID查询区块.jpg"/ alt="根据交易ID查询区块">
</div>

##### 8) 根据HASH查询区块
打开安装链码测试项，以POST请求方式调用http://192.168.235.7:8080/chainblock/queryBlockByHash接口，如果调用成功，以json方式返回结果，请求参数、返回字符串及界面如下所示：
请求参数：
{"hash":"b81a92fc5f0427e17cecf461efcf05733b084d691ccf6b56c51f9a94bffa8e27"}
返回字符串：
{
   "data":    {
      "dataHash": "2a1564a54721489baf18388e4d7429259e39c48fb63eedc4520286f3966da845",
      "blockNumber": 2,
      "calculatedBlockHash": "b81a92fc5f0427e17cecf461efcf05733b084d691ccf6b56c51f9a94bffa8e27",
      "envelopeCount": 1,
      "envelopes": [      {
         "transactionEnvelopeInfo":          {
            "transactionActionInfoArray": [            {
               "chaincodeInputArgsCount": 4,
               "endorserInfoArray": [               {
                  "mspId": "Org1MSP",
                  "signature": "304402201640b5d2507e5d12342067643d6626b61804f53be93b11b81eef1a40269d08c802203d2325ad76b273d664a1f81574d0338d7b1d7a8c6fd0e7007ed9d85cdfc271ce",
                  "id": "-----BEGIN CERTIFICATE-----\nMIICKDCCAc+gAwIBAgIRALGx/vw0QOlTBNy0sDFLGkAwCgYIKoZIzj0EAwIwczEL\nMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFjAUBgNVBAcTDVNhbiBG\ncmFuY2lzY28xGTAXBgNVBAoTEG9yZzEuZXhhbXBsZS5jb20xHDAaBgNVBAMTE2Nh\nLm9yZzEuZXhhbXBsZS5jb20wHhcNMTgwOTI0MTM1ODM5WhcNMjgwOTIxMTM1ODM5\nWjBqMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMN\nU2FuIEZyYW5jaXNjbzENMAsGA1UECxMEcGVlcjEfMB0GA1UEAxMWcGVlcjAub3Jn\nMS5leGFtcGxlLmNvbTBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IABNqgityE093u\n0GEJMj10rjp/VV//BctP3YabP08W/zLcWoCJNiAa3Zu++yiKaIKxhIRmeiAZ9rLF\nnrHP/nhBZ8qjTTBLMA4GA1UdDwEB/wQEAwIHgDAMBgNVHRMBAf8EAjAAMCsGA1Ud\nIwQkMCKAIL71mqycUvRcWvL+HPSBZFM0neNouwtaALLOlAO3i0yIMAoGCCqGSM49\nBAMCA0cAMEQCICQa2yat4HdN55N2n8AsJIyKexeMC33y6WOrh2U2z50lAiB06y8W\nXsLSnCpwwfKIIxagUvMxTb9Inre8upwtFVUwmA==\n-----END CERTIFICATE-----\n"
               }],
               "payload": "",
               "argArray":                [
                  "invoke",
                  "b",
                  "a",
                  "5"
               ],
               "endorsementsCount": 1,
               "rwsetInfo":                {
                  "nsRwsetInfoArray":                   [
                                          {
                        "writeSet": [],
                        "readSet": [                        {
                           "readSetIndex": 0,
                           "readVersionTxNum": 0,
                           "readVersionBlockNum": 1,
                           "namespace": "lscc",
                           "version": "[1 : 0]",
                           "key": "mycc"
                        }]
                     },
                                          {
                        "writeSet":                         [
                                                      {
                              "writeSetIndex": 0,
                              "namespace": "mycc",
                              "value": "205",
                              "key": "a"
                           },
                                                      {
                              "writeSetIndex": 1,
                              "namespace": "mycc",
                              "value": "395",
                              "key": "b"
                           }
                        ],
                        "readSet":                         [
                                                      {
                              "readSetIndex": 0,
                              "readVersionTxNum": 0,
                              "readVersionBlockNum": 1,
                              "namespace": "mycc",
                              "version": "[1 : 0]",
                              "key": "a"
                           },
                                                      {
                              "readSetIndex": 1,
                              "readVersionTxNum": 0,
                              "readVersionBlockNum": 1,
                              "namespace": "mycc",
                              "version": "[1 : 0]",
                              "key": "b"
                           }
                        ]
                     }
                  ],
                  "nsRWsetCount": 2
               },
               "responseStatus": 200,
               "responseMessageString": "",
               "status": 200
            }],
            "txCount": 1,
            "isValid": true,
            "validationCode": 0
         },
         "createId": "-----BEGIN CERTIFICATE-----\nMIICKzCCAdGgAwIBAgIRAKIquOLmST9u5iLYTmTbXxgwCgYIKoZIzj0EAwIwczEL\nMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFjAUBgNVBAcTDVNhbiBG\ncmFuY2lzY28xGTAXBgNVBAoTEG9yZzEuZXhhbXBsZS5jb20xHDAaBgNVBAMTE2Nh\nLm9yZzEuZXhhbXBsZS5jb20wHhcNMTgwOTI0MTM1ODM5WhcNMjgwOTIxMTM1ODM5\nWjBsMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMN\nU2FuIEZyYW5jaXNjbzEPMA0GA1UECxMGY2xpZW50MR8wHQYDVQQDDBZBZG1pbkBv\ncmcxLmV4YW1wbGUuY29tMFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEaZh8qgfm\ny1v9CLdWHuPYMc/lNyGW2TPS1ARdi+CCoO7o6p5BFu/1KOrL7wHFyD0Oc7VOC/uA\nWzPfcnLsyyqLnKNNMEswDgYDVR0PAQH/BAQDAgeAMAwGA1UdEwEB/wQCMAAwKwYD\nVR0jBCQwIoAgvvWarJxS9Fxa8v4c9IFkUzSd42i7C1oAss6UA7eLTIgwCgYIKoZI\nzj0EAwIDSAAwRQIhALR8vjnvpiPqjhVjo+CcFIhqU3W69eGSP39EdIMFHg+fAiAM\njUCAQ7Nk+2LrR62w14/llpT5rm1p8o4WVZ/8Lqq5Bw==\n-----END CERTIFICATE-----\n",
         "isValid": true,
         "validationCode": 0,
         "type": "TRANSACTION_ENVELOPE",
         "nonce": "1201de624aa7736798d800795feefee3162e9d62f42e284a",
         "channelId": "mychannel",
         "transactionID": "5412bf55447701c58753dc90416e6ddcafc3c280fc5585b7316fbd97e9f5d3fb",
         "createMSPID": "Org1MSP",
         "timestamp": "2018/11/08 22:07:16"
      }],
      "previousHashID": "f222ed19805574813c4a4148efc3e9352d6e498e8048719bb379fd65f3fe2561"
   },
   "status": 200
}
界面：
<div align=center>
   <img width="600" src="https://github.com/dragon-lin/fabricClientRest/raw/master/readme-img/根据Hash查询区块.jpg"/ alt="根据HASH查询区块">
</div>
