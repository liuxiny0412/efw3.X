<H1>To Draw Barcode</H1>

To draw barcode in html, you can use drawServlet.
It is almost based on <a href="http://barcode4j.sourceforge.net/">barcode4j</a> except QR Code
which is powered by <a href="https://github.com/zxing/zxing">ZXing</a>.
<h2>Sample for JSP</h2>
<pre>
	&lt;img src="drawServlet?type=qrcode&msg=hellword">
</pre>

<h2>API</h2>
<table>
	<tr><th>Type</th><th>Description</th></tr>
	<tr><td>qrcode</td><td>QR Code</td><td><img src="barcode.qrcode.png"></td></tr>
	<tr><td>codabar</td><td>Codabar</td><td><img src="barcode.codabar.png"></td></tr>
	<tr><td>code39</td><td>Code 39</td><td><img src="barcode.code39.png"></td></tr>
	<tr><td>code128</td><td>Code 128</td><td><img src="barcode.code128.png"></td></tr>
	<tr><td>2of5</td><td>Interleaved 2 of 5</td><td><img src="barcode.2of5.png"></td></tr>
	<tr><td>itf14</td><td>ITF-14</td><td><img src="barcode.itf14.png"></td></tr>
	<tr><td>ean13</td><td>EAN-13</td><td><img src="barcode.ean13.png"></td></tr>
	<tr><td>ean8</td><td>EAN-8</td><td><img src="barcode.ean8.png"></td></tr>
	<tr><td>upca</td><td>UPC-A</td><td><img src="barcode.upca.png"></td></tr>
	<tr><td>upce</td><td>UPC-E</td><td><img src="barcode.upce.png"></td></tr>
	<tr><td>postnet</td><td>POSTNET</td><td><img src="barcode.postnet.png"></td></tr>
	<tr><td>rmcbc</td><td>Royal Mail Customer Barcode</td><td><img src="barcode.rmcbc.png"></td></tr>
	<tr><td>usps4cb</td><td>USPS Intelligent Mail</td><td><img src="barcode.usps4cb.png"></td></tr>
	<tr><td>pdf417</td><td>PDF417</td><td><img src="barcode.pdf417.png"></td></tr>
	<tr><td>datamatrix</td><td>DataMatrix</td><td><img src="barcode.datamatrix.png"></td></tr>
</table>
