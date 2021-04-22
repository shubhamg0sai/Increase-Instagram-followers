g='\033[1;32m'
p='\033[1;35m'
clear
echo -e "\033[1;32m"
cd
clear
termux-setup-storage
clear
apt update -y
echo -e $g 'Please Wait ===+['$p'>              '$g']\'
sleep 0.4
apt upgrade -y
clear
echo -e $g 'Please Wait ===+['$p'-------------> '$g']\'
sleep 0.4
apt install git -y
pkg install python
pkg install python2 -y
clear
echo -e $g 'Please Wait ===+['$p'-------------->'$g']|'
sleep 0.4
pip2 install requests
pip2 install mechanize
clear 
echo -e "$g+++++++++++++++>$p[Please Wait]$g<++++++++++++++"
sleep 0.4
cd
git clone https://github.com/ShuBhamg0sain/Increase-Instagram-followers
cd Increase-Instagram-followers
chmod +x * S.py
clear
echo -e "$g+++++++++++>[$pWelcome to the new update$p$g]<+++++++++++++"
sleep 2 
python2 S.py
