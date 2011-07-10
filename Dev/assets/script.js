function gup( name )
{
  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regexS = "[\\?&]"+name+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var results = regex.exec( window.location.href );
  if( results == null )
	return "";
  else
	return results[1];
}

function WriteImage( name ,w, h)
{
	document.write('<img height="'+h+'" width="'+w+'" src='+rootDir+name+'></img><br>')
}

function WriteLink( link, text )
{
	document.write('<a href="'+rootDir+link+'">'+text+'</a>')
}
var rootDir="./";

document.write('<meta http-equiv="content-type" content="text/html; charset=UTF-8">');
document.write('<link href="http://fonts.googleapis.com/css?family=Lobster&v1" rel="stylesheet" type="text/css">');
document.write('<link rel="stylesheet" type="text/css" href="./theme.css" />');
