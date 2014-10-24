Showtime Remote
===============
Use your Android phone as a remote for Showtime (https://showtimemediacenter.com/)

Status
------
The current version of the app has a grid of 3 times 3 buttons, see the table below for functionality. There is an additional 10th button which can send all of the available actions, see https://showtimemediacenter.com/projects/showtime/wiki/HTTP_API. It also includes a settings screen where it is possible to set the ip address (and port number).

| Button | OnClick         | OnLongClick          |
|--------|-----------------|----------------------|
| HOME   | ACTION_HOME     | -                    |
| UP     | ACTION_UP       | ACTION_TOP           |
| MENU   | ACTION_MENU     | ACTION_ITEMMENU      |
| LEFT   | ACTION_LEFT     | ACTION_SEEK_BACKWARD |
| OK     | ACTION_ACTIVATE | -                    |
| RGHT   | ACTION_RIGHT    | ACTION_SEEK_FORWARD  |
| BACK   | ACTION_NAV_BACK | -                    |
| DOWN   | ACTION_DOWN     | ACTION_BOTTOM        |
| FWD    | ACTION_NAV_FWD  | -                    |

License
-------
GNU GPLv3

-------
(c) 2014 Claes Hallström
