#!/usr/bin/perl
# This file's japanese character sets is EUC.
# This file's line feed code is LF.

read(STDIN, $query_string, $ENV{'CONTENT_LENGTH'});
@posted_params = split(/&/, $query_string);
# 將「變數名稱=值」的配對存入雜湊裡
foreach $posted_param (@posted_params) {
	($name, $value) = split(/=/, $posted_param);
	$form{$name} = $value;
}

print <<END_OF_TEXT;
Content-type: text/html

<html>
<head>
<title>Sugoroku</title>
</head>
<applet code=Sugoroku archive=Sugoroku.jar width=300 height=400>
<param name="name" value="$form{'name'}">
</applet>
</html>
END_OF_TEXT;
