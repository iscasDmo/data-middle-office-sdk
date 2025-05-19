package cn.ac.iscas.dmo.api.sdk.model;



/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/6/11 17:33
 */

public class  FileAssetPortalVO {

    /**文件名*/
    private String name;

    /**匹配片段*/
    private String highlights;

    /**文件路径*/
    private String path;

    /**标签*/
    private String labels;

    /**数据源*/
    private String datasourceName;

    /**数据源类型*/
    private String datasourceType;

    /**文件大小*/
    private String size;

    /**最后修改时间*/
    private String lastModify;

    /**授权状态*/
    private String authStatus = "0";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public String getDatasourceType() {
        return datasourceType;
    }

    public void setDatasourceType(String datasourceType) {
        this.datasourceType = datasourceType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLastModify() {
        return lastModify;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }
}
