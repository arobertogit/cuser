<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Welcome to CuSEr!</title>
    <link rel="stylesheet" type="text/css" href="/resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/resources/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/resources/css/main.css">

    <script type="text/javascript" src="/resources/js/jquery-1.11.3.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
    <script src="https://w.soundcloud.com/player/api.js"
            type="text/javascript"></script>
    <script src="https://connect.soundcloud.com/sdk/sdk-3.0.0.js"></script>

</head>
<body>
<ul class="nav nav-tabs" id="topMenu">
    <li class="active"><a href="#menu" data-toggle="tab">Menu</a></li>
    <li><a href="#images" data-toggle="tab">Images</a></li>
    <li><a href="#videos" data-toggle="tab" id="videosTopMenu">Videos</a></li>
    <li><a href="#jokes" data-toggle="tab">Jokes</a></li>
    <li><a href="#music" data-toggle="tab" id="musicTopMenu">Music</a></li>
    <li><a href="#yesorno" data-toggle="tab">Yes or No</a></li>
    <li><a href="#gifs" data-toggle="tab">GIFs</a></li>
    <li><a href="#cats" data-toggle="tab">Cats</a></li>
    <li><a href="#news" data-toggle="tab">News NYT</a></li>
    <li><a href="#numbers" data-toggle="tab">Random Facts</a></li>
    <li><a href="#api" data-toggle="tab">Api</a></li>
</ul>
<div class="tab-content">
    <div class="tab-pane fade active in" id="menu">
        <ul id="inner-nav-menu" class="nav nav-tabs">
            <li class="inner-menu active"><a href="#breakfast" data-toggle="tab" aria-expanded="true">Breakfast</a></li>
            <li class="inner-menu"><a href="#lunch" data-toggle="tab" aria-expanded="false">Lunch</a></li>
            <li class="inner-menu"><a href="#dinner" data-toggle="tab" aria-expanded="false">Dinner</a></li>
            <li class="inner-menu"><a href="#order" data-toggle="tab" aria-expanded="false">Finish Order</a></li>
        </ul>
        <div class="tab-content">
                <div class="tab-pane fade active in" id="breakfast">
                    <div class="alert alert-success own-area">
                        <div id="breakfast-area">

                        </div>
                    </div>
                </div>
                <div class="tab-pane fade" id="lunch">
                    <div class="alert alert-success own-area">
                        <div id="lunch-area">

                        </div>
                    </div>
                </div>
                <div class="tab-pane fade" id="dinner">
                    <div class="alert alert-success own-area">
                        <div id="dinner-area">

                        </div>
                    </div>
                </div>
                <div class="tab-pane fade" id="order">
                    <div class="alert alert-success own-area">
                        <h3>Finish your order</h3>

                        <div id="order-area">

                        </div>
                        <div id="order-food">

                            <select name="typeOfOccasion" id="typeOfOccasion" class="form-control">
                                <option>business</option>
                                <option>family</option>
                                <option>date</option>
                                <option>single</option>
                            </select>

                            <button id="requestRecommendations" class="btn btn-default next-button">Submit</button>
                        </div>
                    </div>
                 </div>
        </div>
    </div>

    <div class="tab-pane fade" id="images">
        <div class="alert alert-success">
            <ul id="imagesArea" class="row effect-4 grid">

            </ul>
            <input value="0" type="hidden" id="imagePageCounter"/>
            <input type="text" class="form-control" id="inputImageCategory"
                   placeholder="Pick a category">
            <button id="nextImages" class="btn btn-default next-button">Next Page</button>
        </div>
    </div>

    <div class="tab-pane fade" id="videos">
        <div class="alert alert-success own-area" id="videoParent">
            <div id="putTheVideoHere"></div>
            <div id="videoError"></div>
            <input value="0" type="hidden" id="videoCounter"/>
            <select name="YTsearch" id="YTsearchList" class="form-control"></select>
            <button id="nextVideo" class="btn btn-default next-button">Next by category</button>
        </div>
    </div>

    <div class="tab-pane fade" id="jokes">
        <ul id="inner-nav" class="nav nav-tabs">
            <li class="inner-menu active"><a href="#norris"
                                             data-toggle="tab" aria-expanded="true">Norris</a></li>
            <li class="inner-menu"><a href="#yoMomma" data-toggle="tab"
                                      aria-expanded="false">Yo Momma</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane fade active in" id="norris">
                <div class="alert alert-success own-area">
                    <div id="joke-area">
                        <div id="joke-area-norris"></div>
                    </div>
                    <button id="nextJokeNorris" class="btn btn-default next-button">Next</button>
                </div>
            </div>
            <div class="tab-pane fade" id="yoMomma">
                <div class="alert alert-success own-area">
                    <div id="joke-area">
                        <div id="joke-area-yomomma"></div>
                    </div>
                    <button id="nextJokeMomma" class="btn btn-default next-button">Next</button>
                </div>
            </div>
        </div>
    </div>

    <div class="tab-pane fade" id="music">
        <div class="alert alert-success own-area" id="musicParent">
            <div id="putTheWidgetHere"></div>
            <div id="musicError"></div>
            <select name="genre" id="genreList" class="form-control"></select>
            <button id="nextSong" class="btn btn-default next-button">Next</button>
        </div>
    </div>

    <div class="tab-pane fade" id="yesorno">
        <div class="alert alert-success own-area">
            <div id="yesOrNoArea">
                <img src="" alt="yesorno" id="yesOrNoImage" class="yesOrNoRemove">
            </div>
            <button id="nextYesOrNo" class="btn btn-default next-button">Next</button>
        </div>
    </div>

    <div class="tab-pane fade" id="gifs">
        <div class="alert alert-success own-area">
            <div id="gifArea">
                <img src="" alt="gif" id="gifImage" class="gifImageRemove">
            </div>
            <input type="text" class="form-control" id="gifInput"
                   placeholder="Keywords">
            <button id="nextGif" class="btn btn-default next-button">Next</button>
        </div>
    </div>

    <div class="tab-pane fade" id="cats">
        <div class="alert alert-success own-area">
            <div id="catArea">
                <img src="" alt="cat" id="catImage" class="catRemove">
            </div>
            <button id="nextCat" class="btn btn-default next-button">Next</button>
        </div>
    </div>

    <div class="tab-pane fade" id="news">
        <div class="alert alert-success own-area">
            <div id="newsArea">

                <h2 id="titleArticle"></h2>

                <h6 id="categoryArticle"></h6>
                <blockquote>
                    <p id="headingArticle"></p>
                    <small><a href="" id="readMoreArticle"
                              class="text-warning">Read More on the NYT</a>
                    </small>
                </blockquote>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <select class="form-control" id="articleList" name="articleList">
                        <option>Random</option>
                        <option>U.S.</option>
                        <option>Arts</option>
                        <option>Blogs</option>
                        <option>Economy</option>
                        <option>Business Day</option>
                        <option>Technology</option>
                        <option>Health</option>
                        <option>Movies</option>
                        <option>Sports</option>
                        <option>World</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <input type="text" class="form-control col-md-6" id="inputYear"
                           placeholder="Pick a year starting 1860"> <input value="0"
                                                                           type="hidden" id="counter"/>

                </div>
            </div>

            <div class="alert alert-dismissible alert-danger" id="yearAlert">
                <button type="button" class="close" id="closeEx">x</button>
                <strong>Oh snap!</strong> There was a problem with the year
                inserted
            </div>

            <button id="nextArticle" class="btn btn-default next-button">Next</button>
        </div>
    </div>

    <div class="tab-pane fade" id="numbers">
        <ul id="inner-nav-numbers" class="nav nav-tabs">
            <li class="inner-menu active"><a href="#trivia"
                                             data-toggle="tab" aria-expanded="true">Trivia</a></li>
            <li class="inner-menu"><a href="#year" data-toggle="tab"
                                      aria-expanded="true">Year</a></li>
            <li class="inner-menu"><a href="#date" data-toggle="tab"
                                      aria-expanded="true">Date</a></li>
            <li class="inner-menu"><a href="#math" data-toggle="tab"
                                      aria-expanded="true">Math</a></li>
        </ul>

        <div class="tab-content">
            <div class="tab-pane fade active in" id="trivia">
                <div class="alert alert-success own-area">
                    <div id="triviaFact"></div>
                    <button id="nextTiviaFact" class="btn btn-default next-button">Next</button>
                </div>
            </div>

            <div class="tab-pane fade in" id="year">
                <div class="alert alert-success own-area">
                    <div id="yearFact"></div>
                    <button id="nextYearFact" class="btn btn-default next-button">Next</button>
                </div>
            </div>

            <div class="tab-pane fade in" id="date">
                <div class="alert alert-success own-area">
                    <div id="dateFact"></div>
                    <div class="row dateRow">
                        <button id="nextDateFact"
                                class="btn btn-default next-button col-md-6 customDateButton">Random
                        </button>
                        <button id="nextTodayDateFact"
                                class="btn btn-default next-button col-md-6 customDateButton">Today's
                            Fact
                        </button>
                    </div>
                </div>
            </div>

            <div class="tab-pane fade in" id="math">
                <div class="alert alert-success own-area">
                    <div id="mathFact"></div>
                    <button id="nextMathFact" class="btn btn-default next-button">Next</button>
                </div>
            </div>
        </div>
    </div>
    <div class="tab-pane fade" id="api">
        <ul id="inner-nav-api" class="nav nav-tabs">
            <li class="inner-menu active"><a href="#music-area" data-toggle="tab" aria-expanded="true">Music Api</a></li>
            <li class="inner-menu"><a href="#video-area" data-toggle="tab"
                                      aria-expanded="false">Video Api</a></li>
            <li class="inner-menu"><a href="#menu-area" data-toggle="tab"
                                      aria-expanded="false">Menu Api</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane fade active in" id="music-area">
                <div class="alert alert-success own-area">
                    <h3>Music Api</h3>
                    <form method="get" action="/rdf/getMusicLinks">
                        <input name="country" type="text" class="form-control spacing" placeholder="Type a country's name"/>

                        <label>Pick a type of meal:</label>
                        <select name="time" class="form-control spacing">
                            <option>breakfast</option>
                            <option>lunch</option>
                            <option>dinner</option>
                        </select>

                        <label>Pick a type of occasion:</label>
                        <select name="type" class="form-control spacing">
                            <option>business</option>
                            <option>family</option>
                            <option>date</option>
                            <option>single</option>
                        </select>

                        <input type="submit" class="btn btn-primary next-button">
                    </form>
                </div>
            </div>
            <div class="tab-pane fade" id="video-area">
                <div class="alert alert-success own-area">
                    <h3>Videos Api</h3>
                    <form method="get" action="/rdf/getVideoLinks">
                        <input name="country" type="text" class="form-control spacing" placeholder="Type a country's name"/>

                        <label>Pick a type of meal:</label>
                        <select name="time" class="form-control spacing">
                            <option>breakfast</option>
                            <option>lunch</option>
                            <option>dinner</option>
                        </select>

                        <label>Pick a type of occasion:</label>
                        <select name="type" class="form-control spacing">
                            <option>business</option>
                            <option>family</option>
                            <option>date</option>
                            <option>single</option>
                        </select>

                        <input type="submit" class="btn btn-primary next-button">
                    </form>
                </div>
            </div>
            <div class="tab-pane fade" id="menu-area">
                <div class="alert alert-success own-area">
                    <h3>Menu Api</h3>
                    <form method="get" action="/rdf/getMenuToHTML">
                        <input type="submit" class="btn btn-primary next-button" value="Get">
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</div>
<script type="text/javascript" src="/resources/js/ajaxRequests.js"></script>
</body>
</html>