import numpy as np
import matplotlib.pyplot as plt
import pickle
from keras.applications.vgg16 import VGG16
from keras.applications.resnet50 import ResNet50, preprocess_input, decode_predictions
from keras.preprocessing import image
from keras.models import Model, load_model
from keras.preprocessing.sequence import pad_sequences

import warnings
warnings.filterwarnings("ignore")

model=load_model('C://Users//Mohini Vaish//Desktop//model_9.h5')
# model._make_predict_function()

model_temp=ResNet50(weights='imagenet',input_shape=(224,224,3))

model_resnet=Model(model_temp.input,model_temp.layers[-2].output)
# model_resnet._make_predict_function()

max_len=35

with open("C://Users//Mohini Vaish//Desktop//word_to_index.pkl",'rb') as w2i:
    word_to_index=pickle.load(w2i)
    
with open("C://Users//Mohini Vaish//Desktop//index_to_word.pkl",'rb') as i2w:
    index_to_word=pickle.load(i2w)

def preprocess_img(img):
    img=image.load_img(img,target_size=(224,224))
    img=image.img_to_array(img)
    img=np.expand_dims(img,axis=0)
    img=preprocess_input(img)
    return img

def encode_image(img):
    img=preprocess_img(img)
    feature_vector=model_resnet.predict(img)
    feature_vector=feature_vector.reshape((1,feature_vector.shape[1]))
    return feature_vector
IMG_PATH="C://Users//Mohini Vaish//Desktop//Images//"
# enc=encode_image(IMG_PATH+"1308617539_54e1a3dfbe.jpg")

def predict_caption(photo):
    in_text="startseq"
    for i in range(max_len):
        sequence=[word_to_index[w] for w in in_text.split() if w in word_to_index]
        sequence=pad_sequences([sequence],maxlen=max_len,padding='post')
        y_predicted=model.predict([photo,sequence])
        y_predicted=y_predicted.argmax()
        word=index_to_word[y_predicted]
        in_text+=(' '+word)
        if word=='endseq':
            break
        final_caption=in_text.split()[1:-1]
        final_caption=' '.join(final_caption)
    return final_caption

def caption_this_image(input_img): 
    photo = encode_image(input_img)
    caption = predict_caption(photo)
    # keras.backend.clear_session()
    return caption
# print(predict_caption(enc))