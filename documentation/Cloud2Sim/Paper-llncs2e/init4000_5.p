# a scatter plot that shows the correlation between
# wind production and total consumption

# This is to set the colors of the plot
set term png background "white"
set key below 
set style line 1 lc rgb "black" lw 1 pt 1
set style line 2 lc rgb "orange" lw 1 pt 1
set style line 3 lc rgb "brown" lw 1 pt 1
set style line 4 lc rgb "yellow" lw 1 pt 1
set style line 5 lc rgb "purple" lw 1 pt 1
set style line 6 lc rgb "green" lw 1 pt 1
set style line 7 lc rgb "purple" lw 1 pt 1
set style line 8 lc rgb "blue" lw 1 pt 1

set output "init4000_4000.png"

set xlabel "Time/ms"
set ylabel "Cloudlet ID"
set yrange [0:4000]

plot "SS.dat" using 7:1 smooth frequency with points ls 1 title 'Finish Time (FCFS-FCFS)', "TT.dat" using 7:1 smooth frequency with points ls 2 title 'Finish Time (RR-RR)', "TS.dat" using 7:1 smooth frequency with points ls 5 title 'Finish Time (RR-FCFS)', "ST.dat"  using 7:1 smooth frequency with points ls 6 title 'Finish Time (FCFS-RR)', "SS.dat" using 6:1 smooth frequency with points ls 3 title 'Start Time (FCFS-FCFS)', "TT.dat" using 6:1 smooth frequency with points ls 4 title 'Start Time (RR-RR)', "TS.dat" using 6:1 smooth frequency with points ls 7 title 'Start Time (RR-FCFS)', "ST.dat" using 6:1 smooth frequency with points ls 8 title 'Start Time (FCFS-RR)'
