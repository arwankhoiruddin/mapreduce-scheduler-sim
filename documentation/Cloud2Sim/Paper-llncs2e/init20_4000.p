# a scatter plot that shows the correlation between
# wind production and total consumption

# This is to set the colors of the plot
set term png background "white"
set key below 
set style line 1 lc rgb "black" lw 1 pt 1
set style line 2 lc rgb "orange" lw 1 pt 1
set style line 3 lc rgb "brown" lw 1 pt 1
set style line 4 lc rgb "yellow" lw 1 pt 1
set style line 5 lc rgb "purple" lw 1 pt 0
set style line 6 lc rgb "green" lw 1 pt 0
set style line 7 lc rgb "purple" lw 1 pt 1
set style line 8 lc rgb "blue" lw 1 pt 0

set output "init20_4000.png"

set xlabel "Cloudlet ID"
set ylabel "Time/ms"

plot "SSSS.dat" u 1:($7) smooth frequency with points ls 1 title 'Finish Time (FCFS-FCFS)', "TSTS.dat" u 1:($7) smooth frequency with points ls 2 title 'Finish Time (RR-RR)', "TSSS.dat" u 1:($7) smooth frequency with points ls 5 title 'Finish Time (RR-FCFS)', "SSTS.dat"  u 1:($7) smooth frequency with points ls 6 title 'Finish Time (FCFS-RR)', "SSSS.dat" u 1:($6) smooth frequency with points ls 3 title 'Start Time (FCFS-FCFS)', "TSTS.dat" u 1:($6) smooth frequency with points ls 4 title 'Start Time (RR-RR)', "TSSS.dat" u 1:($6) smooth frequency with points ls 7 title 'Start Time (RR-FCFS)', "SSTS.dat" u 1:($6) smooth frequency with points ls 8 title 'Start Time (FCFS-RR)'
