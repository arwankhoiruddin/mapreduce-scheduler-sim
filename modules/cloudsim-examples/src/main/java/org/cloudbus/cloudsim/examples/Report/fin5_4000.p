# This is to set the colors of the plot
set term png background "white"
set key below 
set style line 1 lc rgb "black" lw 1 pt 2
set style line 2 lc rgb "red" lw 1 pt 1
set style line 3 lc rgb "blue" lw 1 pt 1
set style line 4 lc rgb "green" lw 1 pt 1
set output "fin5_4000.png"

set xlabel "Cloudlet ID"
set ylabel "Finish Time/ms"

plot "TT.dat" u 1:($7) with points ls 2 title 'RR-RR', "TS.dat" u 1:($7) with points ls 3 title 'RR-FCFS', "SS.dat" u 1:($7) with points ls 1 title 'FCFS-FCFS', "ST.dat"  u 1:($7) with points ls 4 title 'FCFS-RR' 
