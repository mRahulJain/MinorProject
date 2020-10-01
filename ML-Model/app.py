import os
import io
import PIL.Image as Image
from array import array
from flask import Flask, render_template, url_for, request, redirect
from captionit import caption_this_image
import warnings
warnings.filterwarnings("ignore")

app = Flask(__name__)

@app.route('/predict', methods=['POST'])
def up():
	data = request.json
	view = data['array']
	binData = [x % 256 for x in view]
	bytes1 = bytearray(binData)
	image = Image.open(io.BytesIO(bytes1))
	image.save("C:\\Users\\Mohini Vaish\\Desktop\\Images\\test.jpg")
	imag="C:\\Users\\Mohini Vaish\\Desktop\\Images\\test.jpg"
	caption = caption_this_image(imag)
	print(caption)
	result_dic = {
			'description' : caption
		}
	return result_dic

app.run(host="localhost", port=int("4090"))

if __name__ == '__main__':
	app.run(debug = True)
