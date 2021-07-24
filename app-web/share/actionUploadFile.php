<?php
    function getUserRoot(){
        $curl = curl_init();
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($curl, CURLOPT_URL, 'http://localhost:6699/web/get_user_root.jsp');

        $sessionKey = $_COOKIE['session_key'];
        $headers = array();
        $headers[] = 'session_key: '.$sessionKey;
        curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);

        $result = curl_exec($curl);
        curl_close($curl);

        return $result;
    }

    function typeCheck($file) {
        if (   (       ($file["type"] == "image/gif")
                    || ($file["type"] == "image/jpeg")
                    || ($file["type"] == "image/png")
                    || ($file["type"] == "image/pjpeg")
               )
                    && ($file["size"] < 10*1024*1024)
           ) {
            return true;
        }
        return false;
    }

    function upload($file, $path){
        $path = $path . '\\';

        if ($_FILES["file"]["error"] > 0) {
            echo "Return Code: " . $file["error"] . "<br />";
            return;
        }

        $label = "";
        if (file_exists($path . $file["name"])) {
            $label = time() . "-";
        }
        $saveName = $label . $file["name"];
        move_uploaded_file($file["tmp_name"], $path . $saveName);

        echo "Stored file: " .  $saveName . "<br />";
    }

    function phase_1(){
        if ($_COOKIE['session_key'] == NULL) {
            echo "un sign in";
            exit;
        }

        $rootPathJson = getUserRoot();
        $path = json_decode($rootPathJson, true)['data'];
        if ($path == NULL) {
            echo $rootPathJson . '<br/>';
            exit;
        }
        return $path;
    }

    function phase_2($path) {
        $file = $_FILES["file"];

        if (!typeCheck($file)) {
            echo "undefined file type: " . $file["type"] . ", size: " . ($file["size"] / 1024) . " Kb<br />";
        } else {
            echo "Upload:    ".  $file["name"] . "<br />";
            echo "Type:      ".  $file["type"] . "<br />";
            echo "Size:      ". ($file["size"] / 1024) . " Kb<br />";
            upload($file, $path);
        }
    }

    $path = phase_1();
    phase_2($path);



