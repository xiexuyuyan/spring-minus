<?php

    $curl = curl_init();
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_URL, 'http://localhost:6699/web/get_user_root.jsp');

    $sessionKey = $_COOKIE['session_key'];
    $headers = array();
    $headers[] = 'session_key: '.$sessionKey;
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);

    $result = curl_exec($curl);

    echo $result;

    curl_close($curl);