function loadVideo(YTS) {

	var api_key = 'AIzaSyCZO2nHBNMSGgRg4VHMZ9P8dWT0H23J-Fc';

	if (YTS == 'random') {
		var availableTags = [ "funny", "cats", "dogs", "memes", "songs", "rap",
				"tutorials", "top10" ];

		search_input = availableTags[Math.floor(Math.random()
				* availableTags.length)];

	}

	else {
		search_input = YTS;
	}

	var pageToken = document.getElementById("nextPage");
	var pageValue = '';
	if (pageToken && pageToken.value) {
		pageValue = '&pageToken=' + pageToken.value;
	}

	var keyword = encodeURIComponent(search_input);
	// Youtube API
	var yt_url = 'https://www.googleapis.com/youtube/v3/search?part=snippet&q='
			+ keyword + '&type=video&videoCaption=closedCaption&key=' + api_key
			+ pageValue + '&format=5&maxResults=50&v=2';

		$.ajax({
				type : 'GET',
				// dataType : 'jsonc',
				url : yt_url,
				success : function(response) {
					if (response.items) {
						var data = response.items[Math.floor(Math.random()
								* response.items.length)];

						var video_id = data.id.videoId;
						var video_title = data.snippet.title;
						var video_next_page = response.nextPageToken;
						// IFRAME Embed for YouTube
						var video_frame = "<iframe width='640' height='385' src='http://www.youtube.com/embed/"
								+ video_id
								+ "' frameborder='0' type='text/html'></iframe>";

						var final = "<h3 id='title'>"
								+ video_title
								+ "</h3><div id='nextPage' style='display: none;'>"
								+ video_next_page + "</div><div>" + video_frame
								+ "</div>";

						$("#putTheVideoHere").html(final); // Result

					} else {
						$("#putTheVideoHere").html(
								"<div id='no'>No Video</div>");
					}
				},
				failure : function() {
					// TODO
				}
			});
}

function loadVideoOptions() {

	var s = document.getElementById('YTsearchList');
	var availableTags = [ "funny", "cats", "dogs", "memes", "songs", "rap",
			"tutorials", "top10" ];

	for (var i = 0; i < availableTags.length; i++) {
		var t = document.createElement("option");
		t.textContent = availableTags[i];
		s.appendChild(t);
	}
}

function loadNorris() {
	$.ajax({
		url : "http://api.icndb.com/jokes/random/",
		success : function(result) {
			obj = result['value'];
			var joke = obj['joke'];
			$("#joke-area-norris").html(joke);
		},
		failure : function() {
			// TODO
		}
	});
}

function loadYoMomma() {
	$.ajax({
		url : "http://api.yomomma.info/",
		type : 'GET',
		dataType : 'json',
		success : function(result) {
			var joke = result['joke'];
			$("#joke-area-yomomma").html(joke);
		},
		failure : function() {
			// TODO
		}
	});
}

function loadYesOrNo() {
	$.ajax({
		url : "http://yesno.wtf/api/",
		success : function(result) {
			obj = result;
			var yesOrNo = obj['image'];
			var img = new Image();
			var div = document.getElementById('yesOrNoArea');
			
			$(".yesOrNoRemove").remove();
			
			img.onload = function() {
				div.appendChild(img);
			};

			img.src = yesOrNo;
			img.id = "yesOrNoImage";
			img.className = "yesOrNoRemove"; 
				
		},
		failure : function() {
			// TODO
		}
	});
}

function loadSong(genre) {
	var client_id = '&client_id=ffcaccc2a3bf0998c26d5a980a8b8607';
	var baseURL = 'http://api.soundcloud.com/tracks?genres=';

	if (genre == 'random') {
		var availableTags = [ "rock", "dance", "electro", "classical", "tango",
				"pop", "country", "indie", "punk", "blues", "jazz", "choral",
				"medieval", "reggae", "hip hop", "opera", "random" ];

		var rand = availableTags[Math.floor(Math.random() * 17)];

		URL = baseURL.concat(rand).concat(client_id);
	}

	else {
		URL = baseURL.concat(genre).concat(client_id);
	}

	$
			.ajax({
				type : 'GET',
				dataType : 'json',
				url : URL,
				success : function(result) {
					var random = Math
							.floor((Math.random() * result.length) + 1);
					var randomObj = result[random];

					var link = randomObj['permalink_url'];
					SC.oEmbed(link, {
						element : document.getElementById('putTheWidgetHere')
					});
				},
				failure : function() {
					document.getElementById('musicError').innerHTML = "There was a problem loading the song, try again";
				}
			});

}

function loadSongOptions() {

	var s = document.getElementById('genreList');
	var availableTags = [ "random", "dance", "electro", "classical", "tango",
			"pop", "country", "indie", "punk", "blues", "jazz", "choral",
			"medieval", "reggae", "hip hop", "opera", "rock" ];

	for (var i = 0; i < availableTags.length; i++) {
		var t = document.createElement("option");
		t.textContent = availableTags[i];
		s.appendChild(t);
	}
}

function loadGIF(tag) {

	if (tag == 'random') {
		var URL = "http://api.giphy.com/v1/gifs/random?api_key=dc6zaTOxFJmzC";
	}

	else {
		var URL = "http://api.giphy.com/v1/gifs/random?api_key=dc6zaTOxFJmzC&tag="
				.concat(tag);
	}
	$.ajax({
		url : URL,
		success : function(result) {
			obj = result;
			var giphyObj = obj['data'];
			var imageURL = giphyObj['image_url'];
			var img = new Image();
			
			var div = document.getElementById('gifArea');
			
			$('.gifImageRemove').remove();
			
			img.onload = function() {
				div.appendChild(img);
			};

			img.src = imageURL;
			img.className = "gifImageRemove";
			img.id= "gifImage";
		},
		failure : function() {
			// TODO
		}
	});
}

function loadCat() {
	$
			.ajax({
				url : "http://thecatapi.com/api/images/get?format=xml&results_per_page=1",
				dataType : "xml",
				success : function(result) {
					var img = new Image();
					var imageURL = $(result).find("url");
					var div = document.getElementById('catArea');

					$('.catRemove').remove();
					
					img.onload = function() {
						div.appendChild(img);
					};
					img.src = imageURL[0].innerHTML;
					img.id = "catImage";
					img.className = "catRemove"

				},
				failure : function() {
					// TODO
				}
			});
}

function loadNews(page, year, category, articleOfPage) {

	var baseURL = "http://api.nytimes.com/svc/search/v2/articlesearch.json?&api-key=f982f1eb93b61a79e622aa952c55878c:4:73935133";

	var URL = baseURL;

	if (page != 'none') {
		URL = URL.concat("&page=").concat(page);
	}

	if (category != 'none') {
		URL = URL.concat("&q=").concat(category);
	}
	var nextYear = year;
	nextYear++;

	if (year != 'none') {
		URL = URL.concat("&begin_date=").concat(year).concat("0101"); // begin
																		// of
																		// the
																		// year
		URL = URL.concat("&end_date=").concat(nextYear).concat("0101"); // beginning
																		// of
																		// the
																		// next
																		// year
	}

	$.ajax({
		url : URL,
		dataType : "json",
		success : function(result) {
			var response = result["response"];
			var articles = response["docs"];
			var anArticle = articles[articleOfPage];

			var readMore = anArticle["web_url"]; // url
			var headline = anArticle["headline"];

			var titleFromNYT = headline["main"]; // title

			var section_name = anArticle["section_name"];

			var snippet = anArticle["snippet"]; // snippet paragraph

			var titleElement = document.getElementById('titleArticle');
			titleElement.innerHTML = titleFromNYT;

			var headingElement = document.getElementById('headingArticle');

			headingElement.innerHTML = snippet;

			document.getElementById("readMoreArticle").href = readMore;
			
			if (section_name == null) {
				$('#categoryArticle').html("");
				
				if(headingElement.innerHTML == ""){
					headingElement.innerHTML = "This article cannot be read here, please go to NYT source";
				}
			
			} else {
				$('#categoryArticle').text(section_name);
			}
		},
		failure : function() {
			// TODO
		}
	});
}

function loadTrivia() {
	$.ajax({
		url : "http://numbersapi.com/random/trivia",
		type : 'GET',
		success : function(result) {
			$("#triviaFact").html(result);
		},
		failure : function() {
			// TODO
		}
	});
}

function loadYear() {
	$.ajax({
		url : "http://numbersapi.com/random/year",
		type : 'GET',
		success : function(result) {
			$("#yearFact").html(result);
		},
		failure : function() {
			// TODO
		}
	});
}

function loadDate() {
	$.ajax({
		url : "http://numbersapi.com/random/date",
		type : 'GET',
		success : function(result) {
			$("#dateFact").html(result);
		},
		failure : function() {
			// TODO
		}
	});
}

function loadCurrentDate() {
	var currentTime = new Date();
	var currentMonth = currentTime.getMonth();
	var currentDay = currentTime.getDate();
	currentMonth ++;
	
	$.ajax({
		url : "http://numbersapi.com/"+currentMonth+"/"+currentDay+"/"+"date",
		type : 'GET',
		success : function(result) {
			$("#dateFact").html(result);
		},
		failure : function() {
			// TODO
		}
	});
}

function loadMath() {
	$.ajax({
		url : "http://numbersapi.com/random/math",
		type : 'GET',
		success : function(result) {
			$("#mathFact").html(result);
		},
		failure : function() {
			// TODO
		}
	});
}

function loadGettyImages(category){
	
	var counterValue = $("#imagePageCounter").val();
	var page = counterValue;
	page ++;
	
	var appendApiKeyHeader = function( xhr ) {
		  xhr.setRequestHeader('Api-Key', 'hn4f7zvbwqdkcdsreutw5xzg');
		};
		
	GetSearchResults();
	
	function GetSearchResults(callback) {
		
	  $.ajax({
	    type: "GET",
	    beforeSend: appendApiKeyHeader,
	    url: "https://connect.gettyimages.com/v3/search/images?phrase="+category+"&page="+page,
	  }).success(function (data, textStatus, jqXHR) { 
	    	
	    	var images = data["images"];
	    		    	
	    	for(var i=0; i<8;i++){
	    		var object = images[i];
	    		var display_sizes = object["display_sizes"];
	    		var element = display_sizes["0"];
	    		var uri = element["uri"];
	    		
	    		var imagesArea = document.getElementById("imagesArea");
	    		
	    		var liNode = document.createElement("li");
	    		liNode.setAttribute("class","col-lg-3 col-md-4 col-xs-6 thumb own-grid");    		
	    		var imgNode = document.createElement("img");
	    		imgNode.setAttribute("src",uri);
	    		imgNode.setAttribute("alt","image"+i);
	    		imgNode.setAttribute("height","200");
	    		imgNode.setAttribute("width","200");
	    		  		
	    		liNode.appendChild(imgNode);
	    		imagesArea.appendChild(liNode);	
	    		 		
	    	}

	    	
	    })
	    .fail(function (data, err) { console.log("error"); });
	}
	document.getElementById("imagePageCounter").value++;
}

$(document).ready(function() {
	loadSong('random');
});

$(document).ready(function() {
	loadYesOrNo();
});

$(document).ready(function() {
	loadVideo('random');
});

$(document).ready(function() {
	loadYoMomma();
});

$(document).ready(function() {
	loadNorris();
});

$(document).ready(function() {
	loadVideoOptions();
});

$(document).ready(function() {
	loadSongOptions();
});

$(document).ready(function() {
	loadGIF('random');
});

$(document).ready(function() {
	loadCat();
});

$(document).ready(function() {
	loadTrivia();
});

$(document).ready(function() {
	loadYear();
});

$(document).ready(function() {
	loadDate();
});

$(document).ready(function() {
	loadMath();
});

$(document).ready(function() {
	loadNews('1', 'none', 'none', '1');
});

$(document).ready(function() {
	loadGettyImages("cat");
});

// clicks

$("#nextVideo").click(function() {
	loadVideo($("#YTsearchList option:selected").text());
});

$("#nextSong").click(function() {
	loadSong($("#genreList option:selected").text());
});

$("#nextYesOrNo").click(function() {
	loadYesOrNo();
});

$("#nextJokeMomma").click(function() {
	loadYoMomma();
});

$("#nextJokeNorris").click(function() {
	loadNorris();
});

$("#nextGif").click(function() {
	var gifInput = document.getElementById('gifInput').value;
	loadGIF(gifInput);
});

$("#nextCat").click(function() {
	loadCat();
});

$("#nextArticle").click(
				function() {

					var year = $('#inputYear').val();

					var counterValue = $("#counter").val();

					var page = Math.floor(counterValue / 10);
					var articleOfPage = counterValue % 10;

					var currentTime = new Date();
					var currentYear = currentTime.getFullYear();

					if ((year != "")
							&& (year.length != 4 || year < '1860' || year > currentYear)) {
						$("#yearAlert").show("fast", "swing");

					}
					var category = $("#articleList option:selected").text();

					if (category == 'Random') {
						category = 'none';
					}

					if (year == "") {
						loadNews(page, currentYear, category, articleOfPage);
					} else {
						loadNews(page, year, category, articleOfPage);
					}

					document.getElementById("counter").value++;

				});

$("#closeEx").click(function() {
	$("#yearAlert").hide("slow", "linear");
});

$("#nextTiviaFact").click(function() {
	loadTrivia();
});

$("#nextYearFact").click(function() {
	loadYear();
});

$("#nextDateFact").click(function() {
	loadDate();
});

$("#nextMathFact").click(function() {
	loadMath();
});

$("#nextTodayDateFact").click(function(){
	loadCurrentDate();
});

$("#nextImages").click(function(){
	var myNode = document.getElementById("imagesArea");
	while (myNode.firstChild) {
	    myNode.removeChild(myNode.firstChild);
	}
	
	var category = $("#inputImageCategory").val();
	if(category != ''){
		loadGettyImages(category);
	}
	else {
		loadGettyImages('cat');
	}
});
 
$("#inputImageCategory").on('input', function(){
	
	var inputBox = document.getElementById("inputImageCategory");

	if(inputBox.value ==  ''){
		console.log("reached here");
		$("#nextImages").text("Next Page");
	}
	else{
		console.log("asdf");
		$("#nextImages").text("Search");
	}
});


