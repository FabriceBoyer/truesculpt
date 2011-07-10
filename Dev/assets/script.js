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

