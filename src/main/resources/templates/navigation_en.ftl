<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed sidebar-toggle"
                    aria-controls="navbar">
                <span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>
            </button>
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">Eyounz Booking System</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="javascript:void(0)"> Welcome ${userName}</a></li>
                <li><a id="j_en"
                       onclick="document.cookie='language=en;path=/';window.location.reload()">English</a>
                </li>
                <li><a id="j_cn"
                       onclick="document.cookie='language=cn;path=/';window.location.reload()">中文</a>
                </li>
                <li><a id="j_logout">Logout</a></li>
            </ul>
        </div>
    </div>
</nav>