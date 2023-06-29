var request = function (){
    var url = new java.net.URL('https://ya.ru');
    var connection = url.openConnection();
    var httpBody = connection.getInputStream();
    var status = connection.getResponseCode();
    return 'Status code: ' + status + ' --- Http body: ' + org.apache.commons.io.IOUtils.toString(httpBody, 'UTF-8');
}
request();