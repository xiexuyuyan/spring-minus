function htmlDirectory(name, time) {
  return `
    <div class="box-row py-2 d-flex position-relative">
      <div class="row-content-icon row-dir-icon">
        <svg class="octicon octicon-file-directory hx_color-icon-directory">
          <path fill-rule="evenodd" d="M1.75 1A1.75 1.75 0 000 2.75v10.5C0 14.216.784 15 1.75 15h12.5A1.75 1.75 0 0016 13.25v-8.5A1.75 1.75 0 0014.25 3h-6.5a.25.25 0 01-.2-.1l-.9-1.2c-.33-.44-.85-.7-1.4-.7h-3.5z"></path>
        </svg>
      </div>
      <div class="row-content-name text-auto-wrap">
        <span class="row-content-name-text">` + name + `</span>
      </div>
      <div class="row-content-time">
        <span class="row-content-time-text">` + time + `</span>
      </div>
    </div>
  `;
}
function htmlFile(name, time) {
  return `
    <div class="box-row py-2 d-flex position-relative">
      <div class="row-content-icon row-file-icon">
        <svg class="octicon octicon-file color-icon-tertiary">
            <path fill-rule="evenodd" d="M3.75 1.5a.25.25 0 00-.25.25v11.5c0 .138.112.25.25.25h8.5a.25.25 0 00.25-.25V6H9.75A1.75 1.75 0 018 4.25V1.5H3.75zm5.75.56v2.19c0 .138.112.25.25.25h2.19L9.5 2.06zM2 1.75C2 .784 2.784 0 3.75 0h5.086c.464 0 .909.184 1.237.513l3.414 3.414c.329.328.513.773.513 1.237v8.086A1.75 1.75 0 0112.25 15h-8.5A1.75 1.75 0 012 13.25V1.75z"></path>
        </svg>
      </div>
      <div class="row-content-name text-auto-wrap">
        <span class="row-content-name-text">` + name + `</span>
      </div>
      <div class="row-content-time">
        <span class="row-content-time-text">` + time + `</span>
      </div>
    </div>
  `;
}


function loadAll(e) {
  $.ajax({
    url: "../actionGetAllFiles.php",
    success: function (result) {
      var resJsonObject = JSON.parse(result);
      var dirJsonObject = new Array();
      var fileJsonObject = new Array();
      for(i=0,j=0,k=0,len=resJsonObject.data.length;i<len;i++) {
        var item = resJsonObject.data[i];
        if(item.type == 1) { // file
          fileJsonObject[j++] = item;
        }
        if(item.type == 2) { // dir
          dirJsonObject[k++] = item;
        }
      }
      for (i=0,len=dirJsonObject.length;i<len;i++){
        var item = dirJsonObject[i];
        e.append(htmlDirectory(item.name, getYMDHMS(item.timestamp)));
      }
      for (i=0,len=fileJsonObject.length;i<len;i++){
        var item = fileJsonObject[i];
        e.append(htmlFile(item.name, getYMDHMS(item.timestamp)));
      }
    },
    error : function() {
      console.log("error");
    }
  });
}
function getYMDHMS(timestamp) {
  let time = new Date(timestamp)
  let year = time.getFullYear()
  let month = time.getMonth() + 1
  let date = time.getDate()
  let hours = time.getHours()
  let minute = time.getMinutes()
  let second = time.getSeconds()
  if (month < 10) { month = '0' + month }
  if (date < 10) { date = '0' + date }
  if (hours < 10) { hours = '0' + hours }
  if (minute < 10) { minute = '0' + minute }
  if (second < 10) { second = '0' + second }
  return year + '-' + month + '-' + date + ' ' + hours + ':' + minute + ':' + second
}
