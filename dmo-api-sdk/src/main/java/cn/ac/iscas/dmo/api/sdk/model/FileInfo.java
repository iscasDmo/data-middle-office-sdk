package cn.ac.iscas.dmo.api.sdk.model;


/**
 * 文件信息
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2022/5/17 14:08
 * @since jdk11
 */
public class FileInfo {
    /**
     * 文件名称
     */
    protected String name;

    /**
     * 父级目录
     */
    protected String parentPath;

    /**
     * 文件路径
     */
    protected String filePath;

    /**
     * 是否是文件夹
     */
    protected boolean dir;

    /**
     * 文件大小
     */
    protected String size;

    /**
     * 字节大小
     * */
    protected long byteSize;

    /**
     * 修改日期
     */
    protected String lastModify;

    /**
     * 用户
     */
    protected String owner;

    /**
     * 用户组
     */
    protected String group;

    /**
     * 副本数
     */
    protected short replication = 0;

    /**
     * 数据块大小
     */
    protected String blockSize;

    /**
     * 权限
     */
    protected String permission;

    /**
     * 深度
     */
    protected int level;

    /**
     * 文件类型
     */
    protected FileType type;

    /**
     * 文件来源
     */
    protected String origin;

    /**
     * 文件冷热类型
     */
    protected String hotCold;

    /**
     * 文件后缀
     */
    protected String suffix;

    /**
     * 文件优先级
     */
    protected String priority;

    /**
     * 文件加密方法
     */
    protected String encrypt;

    /**
     * 病毒扫描结果
     * */
    protected Byte virusStatus;


    /**
     * 病毒扫描结果
     * */
    protected String virusStatusStr;


    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public FileInfo doSetFileType() {
        if (dir) {
            type = FileType.FOLDER;
        } else {
            String lowerName = name.toLowerCase();
            // 暂时以后缀名判断文件类型 todo
            if (lowerName.endsWith(".doc") || lowerName.endsWith(".docx")) {
                type = FileType.DOC;
            } else if (lowerName.endsWith(".ppt") || lowerName.endsWith(".pptx")) {
                type = FileType.PPT;
            } else if (lowerName.endsWith(".txt")) {
                type = FileType.TXT;
            } else if (lowerName.endsWith(".pdf")) {
                type = FileType.PDF;
            } else if (lowerName.endsWith(".xls") || lowerName.endsWith(".xlsx")) {
                type = FileType.XLS;
            } else if (lowerName.endsWith(".gif") || lowerName.endsWith(".tiff") || lowerName.endsWith(".jpg") || lowerName.endsWith(".bmp") ||
                    lowerName.endsWith(".jpeg") || lowerName.endsWith(".png")) {
                type = FileType.IMAGE;
            } else if (lowerName.endsWith(".mp3") || lowerName.endsWith(".wma") || lowerName.endsWith(".wav") ||
                    lowerName.endsWith(".midi") || lowerName.endsWith(".mod") || lowerName.endsWith(".st3") || lowerName.endsWith(".xt") ||
                    lowerName.endsWith(".s3m") || lowerName.endsWith(".far") || lowerName.endsWith(".669") || lowerName.endsWith(".ra") ||
                    lowerName.endsWith(".rmx") || lowerName.endsWith(".cmf") || lowerName.endsWith(".cda") || lowerName.endsWith(".mid") ||
                    lowerName.endsWith(".au") || lowerName.endsWith(".mp1") || lowerName.endsWith(".mp2") || lowerName.endsWith(".ogg")) {
                // 按照这个链接判断的 https://www.lishixinzhi.com/aa/2148999.html
                type = FileType.AUDIO;
            } else if (lowerName.endsWith(".avi") || lowerName.endsWith(".wmv") || lowerName.endsWith(".mpg") || lowerName.endsWith(".mpeg") ||
                    lowerName.endsWith(".mov") || lowerName.endsWith(".rm") || lowerName.endsWith(".ram") || lowerName.endsWith(".swf") ||
                    lowerName.endsWith(".flv") || lowerName.endsWith(".mp4")) {
                // 按照这个链接判断的 https://www.lingfenmao.com/it/5092.html
                type = FileType.VIDEO;
            } else if (lowerName.endsWith(".zip")) {
                type = FileType.ZIP;
            } else if (lowerName.endsWith(".rar")) {
                type = FileType.RAR;
            } else if (lowerName.endsWith(".7z")) {
                type = FileType._7Z;
            } else if (lowerName.endsWith(".tar") || lowerName.endsWith(".tar.gz")) {
                type = FileType.TAR;
            } else {
                type = FileType.UNKNOW;
            }
        }
        return this;
    }

    public enum FileType {
        /**
         * 文件夹
         */
        FOLDER,

        /**
         * doc docx
         */
        DOC,

        /**
         * PPT
         */
        PPT,

        /**
         * 文本
         */
        TXT,

        /**
         * PDF
         */
        PDF,

        /**
         * EXCEL
         */
        XLS,

        /**
         * 图片
         */
        IMAGE,

        /**
         * 声音文件
         */
        AUDIO,

        /**
         * 视频
         */
        VIDEO,

        /**
         * ZIP
         */
        ZIP,

        /**
         * RAR
         */
        RAR,

        /**
         * 7Z
         */
        _7Z,

        /**
         * TAR
         */
        TAR,

        /**
         * 未知
         */
        UNKNOW,

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isDir() {
        return dir;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getByteSize() {
        return byteSize;
    }

    public void setByteSize(long byteSize) {
        this.byteSize = byteSize;
    }

    public String getLastModify() {
        return lastModify;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public short getReplication() {
        return replication;
    }

    public void setReplication(short replication) {
        this.replication = replication;
    }

    public String getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(String blockSize) {
        this.blockSize = blockSize;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getHotCold() {
        return hotCold;
    }

    public void setHotCold(String hotCold) {
        this.hotCold = hotCold;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public Byte getVirusStatus() {
        return virusStatus;
    }

    public void setVirusStatus(Byte virusStatus) {
        this.virusStatus = virusStatus;
    }

    public String getVirusStatusStr() {
        return virusStatusStr;
    }

    public void setVirusStatusStr(String virusStatusStr) {
        this.virusStatusStr = virusStatusStr;
    }
}
