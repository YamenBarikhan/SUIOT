import matplotlib.colors as m_lib_colors

color_to_return = ""
final_color = ""
colors = ["white", 'black', 'bisque', 'forestgreen', 'slategrey', 'dimgray', 'darkorange', 'limegreen', 'lightsteelblue', 'dimgrey', 'burlywood', 'darkgreen', 'cornflowerblue', 'gray', 'antiquewhite', 'green', 'royalblue', 'grey', 'tan', 'lime', 'ghostwhite', 'darkgray', 'navajowhite', 'seagreen', 'lavender', 'darkgrey', 'blanchedalmond', 'mediumseagreen', 'midnightblue', 'silver', 'papayawhip', 'springgreen', 'navy', 'lightgray', 'moccasin', 'mintcream', 'darkblue', 'lightgrey', 'orange', 'mediumspringgreen', 'mediumblue', 'gainsboro', 'wheat', 'mediumaquamarine', 'blue', 'whitesmoke', 'oldlace', 'aquamarine', 'slateblue', 'white', 'floralwhite', 'turquoise', 'darkslateblue', 'snow', 'darkgoldenrod', 'lightseagreen', 'mediumslateblue', 'rosybrown', 'goldenrod', 'mediumturquoise', 'mediumpurple', 'lightcoral', 'cornsilk', 'azure', 'blueviolet', 'indianred', 'gold', 'lightcyan', 'purple', 'brown', 'lemonchiffon', 'paleturquoise', 'indigo', 'firebrick', 'khaki', 'darkslategray', 'darkorchid', 'maroon', 'palegoldenrod', 'darkslategrey', 'darkviolet', 'darkred', 'darkkhaki', 'teal', 'mediumorchid', 'red', 'ivory', 'darkcyan', 'thistle', 'mistyrose', 'beige', 'aqua', 'plum', 'violet', 'salmon', 'lightyellow', 'cyan', 'tomato', 'lightgoldenrodyellow', 'darkturquoise', 'purple', 'darksalmon', 'olive', 'cadetblue', 'darkmagenta', 'coral', 'yellow', 'powderblue', 'fuchsia', 'orangered', 'olivedrab', 'lightblue', 'magenta', 'lightsalmon', 'yellowgreen', 'deepskyblue', 'orchid', 'sienna', 'darkolivegreen', 'skyblue', 'mediumvioletred', 'seashell', 'greenyellow', 'lightskyblue', 'deeppink', 'chocolate', 'chartreuse', 'steelblue', 'hotpink', 'saddlebrown', 'lawngreen', 'aliceblue', 'lavenderblush', 'sandybrown', 'honeydew', 'dodgerblue', 'palevioletred', 'peachpuff', 'darkseagreen', 'lightslategray', 'crimson', 'peru', 'palegreen', 'lightslategrey', 'pink', 'linen', 'lightgreen', 'slategray', 'lightpink']


def new_set_color_string(initial_list):
    color_string = ''.join(initial_list)
    if (color_string == "lightgreen"):
        color_to_return = "lawngreen"
    elif (color_string == "green"):
        color_to_return = "lime"
    elif (color_string == "darkgreen"):
        color_to_return = "darkgreen"
    elif (color_string == "red"):
        color_to_return = "red"
    elif (color_string == "lightred"):
        return 255, 5, 5
    elif (color_string == "darkred"):
        color_to_return = "darkred"
    elif (color_string == "blue"):
        color_to_return = "blue"
    elif (color_string == "lightblue"):
        color_to_return = "aqua"
    elif (color_string == "darkblue"):
        color_to_return = "navy"
    elif (color_string == "purple"):
        color_to_return = "darkviolet"
    elif (color_string == "darkpurple"):
        color_to_return = "indigo"
    elif (color_string == "lightpurple"):
        color_to_return = "magenta"
    elif (color_string == "yellow"):
        color_to_return = "yellow"
    elif (color_string == "orange"):
        color_to_return = "orange"
    elif (color_string == "darkorange"):
        color_to_return = "darkorange"
    elif (color_string == "white"):
        color_to_return = "white"
    elif(color_string == "lightred"):
        return 255, 5, 5
    elif(color_string in colors):
        return string_to_rgb(color_string)
    elif(color_string not in colors):
        return 255, 255, 255
    return string_to_rgb(color_to_return)
def string_to_rgb(input):
    rgb = m_lib_colors.CSS4_COLORS.get(input.lower())
    if rgb:
        rgb_color = tuple(int(color * 255) for color in m_lib_colors.to_rgba(rgb)[:3])
        return rgb_color
    else:
        return None

