//  cNamed.java -- the c1 palette based on named colors

package rts;

import java.awt.*;



final class cNamed extends cPalette {



cNamed() {

    chars = vString.New("0123456789?!nNAa#@oOBb$%pPCc&|qQDd,." +
	"rREe;:sSFf+-tTGg*/uUHh`'vVIi<>wWJj()xXKk[]yYLl{}zZMm^=");

    int j = 0;
    for (int i = 0; i < chars.length(); i++) {
	char c = chars.charAt(i);
	double r = values[j++] / 48.0;
	double g = values[j++] / 48.0;
	double b = values[j++] / 48.0;
	entry[c] = new cEntry(r, g, b, true, false);
    }
    
}



static final int cubeside = 10;		// size of mapping cube
static String mapcube =			// one thousand points of light
    "00wwwWWWJJ00vvvWWWJJsttvvVVVVJsttuuVVVVIsttuuVVVIISSTTTUUUII" +
    "SSTTTUUUUISSTTTUUUUHFFTTGGUUHHFFFGGGGHHH00xxxWWWJJ!11vvWWWJJ" +
    "r11vvVVVjjrrtuuVVVjjrrtuuVVVIISSTTTUUUIISSTTTUUUiiSSTTTUUUii" +
    "FFffGGggHHFFffGGggHHnzxxxXXXXJ!11xxXXXjjp1122)Vjjjrr222)Vjjj" +
    "rr222>>>jjRR--//>>iiRRRT//'iiiRRff//ggiiRffffggghhFffffggghh" +
    "nzzyyXXXXKoozyyXXXjjpp222)Xjjjqq222))jjjqq222>>>jjRR--//>>ii" +
    "RRR-//''iiRRff//'giiRffffggghhEffffggghhnzzyyXXXKKoozyyXXXKK" +
    "???22]]]jjqq222]]]jjqq2233>>jjRR::33>>iiRR:::/''iiRR:::/''ii" +
    "EEfffggghhEEfffggghhNNZZZYYYKKOOZZZYYYKK???@=]]]kkPP@@=]]]kk" +
    "PP%%%3]]kkQQ||3344((QQ:::444((QQ:::444<<EEeee++**<EEeee++**`" +
    "NNZZZYYYYKOOZZZYYYkkOOOZ==}kkkPPP@==}}kkPP%%%=}}kkQQ|||444((" +
    "QQ...444((QQe..444<<Qeeee++**<Eeeee++**`NNZZZYYYYLOOZZZYYYkk" +
    "OOOa==mkkkPPaa==}kkkPP%%%=}888QQ|||44888QQc..444[[QQcc99455<" +
    "Qeeee;;55<Deeee;;;*`AAZZMMYYLLAAaaMMmmLLBBaaammmllBBaaammmll" +
    "CCbbb77788CCbbb77^[[Qcccc##^[[Qccc99$55[DDdd9&&556DDddd;;;66" +
    "AAAMMMMLLLAAaaMMmmLLBBaaammmllBBaaammmllCCbbb777llCCbbb77^^{" +
    "Ccccc##^^{Dccc99$$^{DDdd9&&&66DDddd,,,66";

vString Key(wColor k) {
    if (k.r == k.g && k.g == k.b) {
	// this is a shade of gray
	return vString.New("0123456".charAt((int) (6 * k.g + 0.5)));
    } else {
	// not a shade of gray -- look up in mapping cube
	int r = (int) (k.r * (cubeside - 1) + 0.5);
	int g = (int) (k.g * (cubeside - 1) + 0.5);
	int b = (int) (k.b * (cubeside - 1) + 0.5);
	int n = (cubeside * cubeside * r) + (cubeside * g) + b;
	return vString.New(mapcube.charAt(n));
    }
}



//  array of r,g,b values scaled 0 - 48

private static byte[] values = new byte[] {
	0, 0, 0,		//  0             black
	8, 8, 8,		//  1   very dark gray
	16, 16, 16,		//  2        dark gray
	24, 24, 24,		//  3             gray
	32, 32, 32,		//  4       light gray
	40, 40, 40,		//  5  very light gray
	48, 48, 48,		//  6             white
	48, 24, 30,		//  7             pink
	36, 24, 48,		//  8             violet
	48, 36, 24,		//  9  very light brown
	24, 12, 0,		//  ?             brown
	8, 4, 0,		//  !   very dark brown
	16, 0, 0,		//  n   very dark red
	32, 0, 0,		//  N        dark red
	48, 0, 0,		//  A             red
	48, 16, 16,		//  a       light red
	48, 32, 32,		//  #  very light red
	30, 18, 18,		//  @        weak red
	16, 4, 0,		//  o   very dark orange
	32, 8, 0,		//  O        dark orange
	48, 12, 0,		//  B             orange
	48, 24, 16,		//  b       light orange
	48, 36, 32,		//  $  very light orange
	30, 21, 18,		//  %        weak orange
	16, 8, 0,		//  p   very dark red-yellow
	32, 16, 0,		//  P        dark red-yellow
	48, 24, 0,		//  C             red-yellow
	48, 32, 16,		//  c       light red-yellow
	48, 40, 32,		//  &  very light red-yellow
	30, 24, 18,		//  |        weak red-yellow
	16, 16, 0,		//  q   very dark yellow
	32, 32, 0,		//  Q        dark yellow
	48, 48, 0,		//  D             yellow
	48, 48, 16,		//  d       light yellow
	48, 48, 32,		//  ,  very light yellow
	30, 30, 18,		//  .        weak yellow
	8, 16, 0,		//  r   very dark yellow-green
	16, 32, 0,		//  R        dark yellow-green
	24, 48, 0,		//  E             yellow-green
	32, 48, 16,		//  e       light yellow-green
	40, 48, 32,		//  ;  very light yellow-green
	24, 30, 18,		//  :        weak yellow-green
	0, 16, 0,		//  s   very dark green
	0, 32, 0,		//  S        dark green
	0, 48, 0,		//  F             green
	16, 48, 16,		//  f       light green
	32, 48, 32,		//  +  very light green
	18, 30, 18,		//  -        weak green
	0, 16, 8,		//  t   very dark cyan-green
	0, 32, 16,		//  T        dark cyan-green
	0, 48, 24,		//  G             cyan-green
	16, 48, 32,		//  g       light cyan-green
	32, 48, 40,		//  *  very light cyan-green
	18, 30, 24,		//  /        weak cyan-green
	0, 16, 16,		//  u   very dark cyan
	0, 32, 32,		//  U        dark cyan
	0, 48, 48,		//  H             cyan
	16, 48, 48,		//  h       light cyan
	32, 48, 48,		//  `  very light cyan
	18, 30, 30,		//  '        weak cyan
	0, 8, 16,		//  v   very dark blue-cyan
	0, 16, 32,		//  V        dark blue-cyan
	0, 24, 48,		//  I             blue-cyan
	16, 32, 48,		//  i       light blue-cyan
	32, 40, 48,		//  <  very light blue-cyan
	18, 24, 30,		//  >        weak blue-cyan
	0, 0, 16,		//  w   very dark blue
	0, 0, 32,		//  W        dark blue
	0, 0, 48,		//  J             blue
	16, 16, 48,		//  j       light blue
	32, 32, 48,		//  (  very light blue
	18, 18, 30,		//  )        weak blue
	8, 0, 16,		//  x   very dark purple
	16, 0, 32,		//  X        dark purple
	24, 0, 48,		//  K             purple
	32, 16, 48,		//  k       light purple
	40, 32, 48,		//  [  very light purple
	24, 18, 30,		//  ]        weak purple
	16, 0, 16,		//  y   very dark magenta
	32, 0, 32,		//  Y        dark magenta
	48, 0, 48,		//  L             magenta
	48, 16, 48,		//  l       light magenta
	48, 32, 48,		//  {  very light magenta
	30, 18, 30,		//  }        weak magenta
	16, 0, 8,		//  z   very dark magenta-red
	32, 0, 16,		//  Z        dark magenta-red
	48, 0, 24,		//  M             magenta-red
	48, 16, 32,		//  m       light magenta-red
	48, 32, 40,		//  ^  very light magenta-red
	30, 18, 24,		//  =        weak magenta-red
};



} // class cNamed
