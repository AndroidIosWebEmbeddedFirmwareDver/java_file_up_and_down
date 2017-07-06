该工程关于文件上传与下载

在com.fileupdown.action包下有两个Action，其中，FileUpAction是文件上传相关的，FileDownAction是文件下载相关的。

FileUpAction支持三种文件上传方式，其中映射为“/upload”的方法，可以支持单文件与多文件上传，单文件上传，只需要前台写如下input框即可：
<input type="file" name="file"/>
如果是多文件上传，多个input框name属性一致即可；
映射为“/testupload”方法适用于单文件上传，小文件上传可以选择这个；
映射为“/filesliceup”方法适用于单文件分片上传，能够减小文件上传过程中因网络原因而出现故障的几率，如果文件片段没有上传正确，将会自动上传错误片段，适用于较大文件（10M级别及以上）上传的场景。

三种文件上传方式都会将上传文件的信息写入数据库，包括文件大小，名字，保存路径等等。com.fileupdown.bean包下的Aaa_file_keep_info类，则是文件信息保存到数据库的pojo类，字段信息参见sql文件。

jsp页面在WEB-INF下的jsp/fileupdown文件夹内，其中filesliceupsec.jsp简单地描述了如何使用文件分片上传功能，需要注意的是，要引入js文件夹下的fileoperate.js

FileDownAction里面映射为“/testdown”的方法，展现了文件从服务器下载的过程，包括从数据库内读取文件信息的过程，对应的参考下载页面为：WEB-INF下jsp/fileupdown的filedown.jsp
