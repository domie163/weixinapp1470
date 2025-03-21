const base = {
    get() {
        return {
            url : "http://localhost:8080/tieludingpiao/",
            name: "tieludingpiao",
            // 退出到首页链接
            indexUrl: 'http://localhost:8080/tieludingpiao/front/index.html'
        };
    },
    getProjectName(){
        return {
            projectName: "铁路订票平台小程序"
        } 
    }
}
export default base
