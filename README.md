<div id="top"></div>

# Object Glorification Android Application

The goal of the project was to create an application that glorifies a specified object within a given image. After the glorification the user should have some sort of ability to “play” with the object by various manipulations on the object.
We decided to achieve this goal by creating an Android application that allows users to select an image, mark an object in that given image and perform the glorification process. In this case glorifying means blurring the background and magnifying the object. After the object is glorified the user can perform manipulations on the object like scaling and rotating. We also decided to add a bonus option - to allow the user to select two objects at once.  

Check out our website: https://shanibar3.wixsite.com/my-site-3


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage Demo</a></li>
    <li><a href="#future">Future Work</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#links">Related Links</a></li>
  </ol>
</details>



<!-- Usage Steps -->
## Usage Steps

![Alt text](./img/explanation.gif?raw=true "Explanation")

1. Image selection - from gallery or take a picture with camera (possible also to select 2 objects)
2. Object marking
3. Glorify the object
4. Clear the mark
5. Object manipulation - zoom-in, rotate
6. Save the result image

<p align="right">(<a href="#top">back to top</a>)</p>



### Built With

* Python 3
* Android studio- native
* Grabcut
* OpenCV package
* Java
* Chaquopy plugin

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->
## Usage Demo

https://www.youtube.com/watch?v=fLY6LCJ2oKE&list=TLGGAQG9--NydiAwNTA3MjAyMw

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- Futer Work -->
## Future Work

- Finer segmentation - the algorithm segments iteratively to get the best result. But in some cases, the
segmentation won't be fine, like, it may have marked some foreground regions as
background and vice versa. In that case the user needs to do fine touch-ups by giving some
strokes on the images where some faulty results are there. The Grabcut function offered by
OpenCV allows you to enter a rectangle to mark the object but also allows you to enter a
mask with the touch-ups. In future work, the segmentation could be improved by allowing the
user to do some touch-ups as well.
 - Improving segmentation preformance - we simplified the Grabcut algorithm by using the OpenCV
implementation instead of the igraph based one since Chaquopy doesn’t support the igraph
package. If in future Chaquopy versions, igraph support will be available we could improve
the Grabcut implementation by using igraph again and by that improve the image processing
performance.
- Improving the UI - currently to segment the object the user needs to mark the object and press a button. In the
future, another more comfortable options could be to simply tap the marked object. This will
give the app a more simplistic UI with fewer buttons.
More possible improvements could be adding more manipulation options to the object like
- Adding manipulation options - moving it around, coloring it, rotating it with fingers only
- Adding an ability to select an infinite number of objects
- Publishing the App - Currently, the app is only available by direct download. We wanted to upload it to the Google Store but this required a fee for both Google and for Chaquopy in order to get a distribution license. In the future, a small investment could allow
the app to be published to the masses.



<p align="right">(<a href="#top">back to top</a>)</p>




<!-- CONTACT -->
## Contact

Shani Bar-Gera: [www.linkedin.com/in/shani-bar-gera-153987186](www.linkedin.com/in/shani-bar-gera-153987186) 

Shani Bigdary

Project Link: [https://github.com/shaniBarGera/curves-graphing-tool.git](https://github.com/shaniBarGera/object-glorification.git)

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- Links -->
## Related Links

* Grabcut Algorithm Theory (https://medium.com/analytics-vidhya/computer-vision-understanding-grabcut-algorithm-without-the-maths-9a97ef4c5ba3)
* (https://docs.opencv.org/3.4/d8/d83/tutorial_py_grabcut.html)
* Chaquopy (https://chaquo.com/chaquopy/)
* OpenCV (https://opencv.org/about/)
* Gradle (https://docs.gradle.org/current/userguide/what_is_gradle.html)
* Chaquopy igraph Issue (https://stackoverflow.com/questions/68977565/trying-to-install-python-igraph-on-chaquopy-android-studio)
* (https://github.com/chaquo/chaquopy/issues/539)
* Segmentation Algorithms (https://datahacker.rs/top-10-github-papers-semantic-segmentation/)
* Exif Interface (https://developer.android.com/jetpack/androidx/releases/exifinterface)
* Chaquopy Tutorial (https://www.youtube.com/watch?v=dFtxLCSu3wQ&ab_channel=ProgrammingFever)


<p align="right">(<a href="#top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/othneildrew/Best-README-Template.svg?style=for-the-badge
[contributors-url]: https://github.com/othneildrew/Best-README-Template/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/othneildrew/Best-README-Template.svg?style=for-the-badge
[forks-url]: https://github.com/othneildrew/Best-README-Template/network/members
[stars-shield]: https://img.shields.io/github/stars/othneildrew/Best-README-Template.svg?style=for-the-badge
[stars-url]: https://github.com/othneildrew/Best-README-Template/stargazers
[issues-shield]: https://img.shields.io/github/issues/othneildrew/Best-README-Template.svg?style=for-the-badge
[issues-url]: https://github.com/othneildrew/Best-README-Template/issues
[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge
[license-url]: https://github.com/othneildrew/Best-README-Template/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/othneildrew
[product-screenshot]: images/screenshot.png
