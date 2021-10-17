<?php
    // 1. 获取从html通过post传递的data
    $jsonString = file_get_contents("php://input");
    // 2. 初始话curl，设置请求链接
    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL, 'http://localhost:6699/com.yuyan.harp/login.jsp');
    // 3. 将json data转置为curl post请求的参数列表
    $reString = "";
    $jsonObject = json_decode($jsonString, true);
    foreach($jsonObject as $k => $v) {
        $reString .= $k.'='.$v."&";
    }
    curl_setopt($curl, CURLOPT_POST, true);
    curl_setopt($curl, CURLOPT_POSTFIELDS, $reString);
    // 4. 设置可以获取响应的header
    curl_setopt($curl, CURLOPT_HEADER, true);
    // 5. 设置以文件流的形式返回值
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    $result = @curl_exec($curl);
    // 6. 获取header
    $responseHeaderSize = curl_getinfo($curl, CURLINFO_HEADER_SIZE);
    $responseHeader = substr($result, 0, $responseHeaderSize);
    // 7. 从header中通过正则过滤需要的键值
    preg_match('/(?<=session_key: )\d{4}/', $responseHeader, $sessionKeyMatches);
    $sessionKey = $sessionKeyMatches[0];
    if (is_null($sessionKey)) {

    } else {
        setcookie('session_key', $sessionKey, time()+3600*24*15, '/');
    }

    echo substr($result, $responseHeaderSize, strlen($result));

    curl_close($curl);
