In \nRF5-SDK-for-Mesh-master\examples\common\include\simple_hal.h file, following line is required to comment out so that we can enable the button on the board:
#define BUTTON_BOARD (defined(BOARD_PCA10040) || defined(BOARD_PCA10028) || defined(BOARD_PCA10056)) //lint -e491 // Suppress "non-standard use of 'defined' preprocessor operator"
