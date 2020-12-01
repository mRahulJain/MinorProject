import os
import io
import PIL.Image as Image
import base64
from flask import Flask, render_template, url_for, request, redirect
from captionit1 import caption_this_image
import warnings
warnings.filterwarnings("ignore")

app = Flask(__name__)
#Change your directory HERE.....
directory = "<Your path here>"
@app.route('/predict', methods=['POST'])
def up():
	data = request.json
	base64String = str(data['string1'])
	bytes1 = base64.b64decode(base64String)
	#binData = [x % 256 for x in view]
	#bytes1 = bytearray(binData)
	image = Image.open(io.BytesIO(bytes1))
	#print(type(image))
	#rs = (224,224)
	#image = image.resize(rs)
	image.save(directory+"test.jpg")
	imag = directory+"test.jpg"
	caption = caption_this_image(imag)
	result_dic = {
			'description' : caption
		}
	return result_dic
	#app.run(host="localhost", port=int("4090"))

if __name__ == '__main__':
#	app.run()
	app.run(debug = True,use_reloader=False,host="192.168.1.12", port=int("4090"))