import numpy as np
import cv2 as cv
from PIL import Image
import base64
import io

def paddedzoom(img, zoomfactor=1.2):
    ' does the same thing as paddedzoom '
    x = img.shape[0:2]
    h, w = x
    M = cv.getRotationMatrix2D((w / 2, h / 2), 0, zoomfactor)
    return cv.warpAffine(img, M, x[::-1])

def edit_pic(data, minx, miny, maxx, maxy):
    decoded_data = base64.b64decode(data)
    np_data = np.frombuffer(decoded_data, np.uint8)
    img = cv.imdecode(np_data, cv.IMREAD_UNCHANGED)
    rect = (minx, miny, maxx, maxy)
    img = paddedzoom(img)
    mask = np.zeros(img.shape[:2], np.uint8)
    bgdModel = np.zeros((1, 65), np.float64)
    fgdModel = np.zeros((1, 65), np.float64)
    cv.grabCut(img, mask, rect, bgdModel, fgdModel, 5, cv.GC_INIT_WITH_RECT)
    mask2 = np.where((mask == 2) | (mask == 0), 0, 1).astype('uint8')
    img = img * mask2[:, :, np.newaxis]
    tmp = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    _, alpha = cv.threshold(tmp, 0, 255, cv.THRESH_BINARY)
    b, g, r = cv.split(img)
    rgba = [r, g, b, alpha]
    dst = cv.merge(rgba, 4)
    pil_im = Image.fromarray(dst)
    buff1 = io.BytesIO()
    pil_im.save(buff1, format="PNG")
    img_str = base64.b64encode(buff1.getvalue())
    return "" + str(img_str, 'utf-8')

def get_background(data):
    decoded_data = base64.b64decode(data)
    np_data = np.frombuffer(decoded_data, np.uint8)
    img = cv.imdecode(np_data, cv.IMREAD_UNCHANGED)
    gray_image = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    img = paddedzoom(img)
    mask = np.zeros(img.shape[:2], np.uint8)
    mask2 = np.where((mask == 2) | (mask == 0), 0, 1).astype('uint8')
    background = cv.GaussianBlur(gray_image, (25, 25), cv.BORDER_DEFAULT) * (1 - mask2[:, :])
    pil_bg = Image.fromarray(background)
    buff1 = io.BytesIO()
    pil_bg.save(buff1, format="PNG")
    bg_str = base64.b64encode(buff1.getvalue())
    return "" + str(bg_str, 'utf-8')

