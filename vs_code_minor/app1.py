import os
import io
import PIL.Image as Image
import base64
from flask import Flask, render_template, url_for, request, redirect
from captionit1 import caption_this_image
import warnings
warnings.filterwarnings("ignore")

app = Flask(__name__)
@app.route('/predict', methods=['POST'])
def up():
	data = request.json
	base64String = str(data['string1'])
	bytes1 = base64.b64decode(base64String)
	image = Image.open(io.BytesIO(bytes1))
	image.save('./test.jpg')
	imag = './test.jpg'
	caption = caption_this_image(imag)
	result_dic = {
			'description' : caption
		}
	return result_dic

if __name__ == '__main__':
	app.run(debug = True,use_reloader=False,host="192.168.29.193", port=int("4090"))