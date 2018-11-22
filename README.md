![logo_round](https://user-images.githubusercontent.com/10103993/48885438-503abd80-ee63-11e8-9ad1-eba0865269b0.png) 



# See My Course

![](https://img.shields.io/badge/CropImage-2.7.%2B-brightgreen.svg?style=flat-square) ![](https://img.shields.io/badge/ButterKnife-9.0.0--rc1-brightgreen.svg?style=flat-square) ![](https://img.shields.io/badge/ML%20Kit-18.0.1-brightgreen.svg?style=flat-square) ![](https://img.shields.io/badge/Firebase-16.0.6-brightgreen.svg?style=flat-square) 



## App Structure

```json
MainActivity
  |
  |———— TodoListFragment
  |———— RecognitionFragment
          |———— MetaText
          |———— TemplateSelectActivity
          |———— PuzzleActivity
                  |———— Puzzle
		          |———— BitmapPiece
  |———— ProductionFragment
  |———— Util
```



## Class Introduction

### MainActivity

主界面

使用 ``FramgLayout`` 切换 不同的 ``Fragment`` .



### TodoListFragment

事项页面

列表方式展示



### RecognitionFragment

识别与编辑页面

- 图片输入
- 画布渲染
- 存储文本
- 切换模板



### ProductionFragment

纯粹地展示模板



### MetaText

识别的文本块

- 文本内容
- 文本块``boundingbox``（记录横纵坐标）



### TemplateSelectActivilty

用于选择模板的Activity

选择的结果将会通过``setResult()`` 函数返回到 ``RecognitionFragment``



### PuzzleActivity

拼图游戏 Activity

### Puzzle

拼图 使用 控件 ``RecyclerView`` 实现

- 使用 ``GridLayoutManager`` 实现网格布局
- 使用``ItemTouchHelper`` 实现拖拽排序

### BitmapPiece

拼图的每一块是一个 ``Bitmap`` ，通过切割生成。

``BitmapPiece`` 存储了切割结束后的每一块的``Bitmap``以及其在原图片的索引。



### Util

所有的工具类函数，包括

- 读写本地文件
- 读写本地存储 (**SharedPreference**)
- **ML Kit** 文本识别
- 切割``Bitmap``

以及需要全局存放的变量

- 切割结束的 ``Bitmap``数组 （因为跨Activity 传输非常麻烦，所以采取了放在全局的折中方案）



### Tetris 

之前实现的一个简易版俄罗斯方块游戏，作为彩蛋？？

