#CYBER NAME BLACK-KILLER
#GITHUB: https://www.instagram.com/shubham_g0sain
import os
CorrectUsername = "g0sain"
CorrectPassword = "follow"

loop = 'true'
while (loop == 'true'):
    username = raw_input("\033[1;96m[#] \x1b[0;36m Enter Username\x1b[1;92m➤ ")
    if (username == CorrectUsername):
    	password = raw_input("\033[1;96m[#] \x1b[0;36m Enter Password\x1b[1;92m➤ ")
        if (password == CorrectPassword):
            print "Logged in successfully as " + username #fb-cloning-id SG
            loop = 'false'
        else:
            print "Wrong password!"
            os.system('xdg-open https://www.instagram.com/shubham_g0sain/?hl=en')
    else:
        print "Wrong username!"
        os.system('xdg-open https://www.instagram.com/shubham_g0sain/?hl=en')
import os,sys,time,datetime,random,hashlib,re,threading,json,urllib,cookielib,getpass
##### Dev : ShuBhamg0sain#####
##### LOGO #####
import os
os.system('clear')
os.system("figlet -f pagga 'increase' | lolcat")
os.system("figlet -f pagga 'instagram' | lolcat")
os.system("figlet -f pagga 'followers' | lolcat")
logo='''
\033[1;92m•◈•───────────────•◈•\033[1;92mShuBhamg0sain\033[1;96m•◈•───────────────•◈•
\x1b[1;93m-------------------------------------------------------------
\x1b[1;96m➣  NAME  : Shubhamg0sain
\x1b[1;91m➣  CYBER NAME : BLACK-KILLER
\x1b[1;93m-------------------------------------------------------------
                                '''
def menu():
        print logo
        print
        print "\033[1;91mserver list "
        print "\033[1;96m[1]  page server 1"
        print "\033[1;96m[2]  page server 2"
        print "\033[1;96m[3]  page server 3"
        print "\033[1;96m[4]  page server 4"
        print "\033[1;96m[5]  page server 5"
        print "\033[1;96m[6]  page server 6"
        print "\033[1;91m[7]  follow me"
        print "\033[1;91m[8]  UPDATE SCRIPT"
        print "\033[1;91m[0]  FOR EXIT"
        print 50*'-'
        action()

def action():
        bch = raw_input('\n  ENTER SERVER NUMBER ')
        if bch =='':
                print "\033[1;91mFill in correctly"
                os.system('xdg-open https://www.instagram.com/shubham_g0sain/?hl=en')
                action()   

        elif bch =="1":
                os.system('xdg-open https://bayitakipci.com/')             
                
        elif bch =="2":
                os.system('xdg-open https://begeni.vip/enter')
                              
        elif bch =="3":
                os.system('xdg-open https://gramtakipci.com/')
                
        elif bch =="4":
                os.system('xdg-open https://www.instagram.com/shubham_g0sai')

        elif bch =="5":
                os.system('xdg-open https://www.instagram.com/shubham_g0sai')
 
        elif bch =="6":
                os.system('xdg-open https://www.instagram.com/shubham_g0sai')
  
        elif bch =="7":
                os.system('xdg-open https://www.instagram.com/shubham_g0sain')
          
        elif bch =="8":  
                os.system("cd")
                os.system("rm -rf Increase-Instagram-followers")
                os.system("git clone https://github.com/ShuBhamg0sain/Increase-Instagram-followers.git")
                os.system("cd Increase-Instagram-followers")
                os.system("python2 S.py")

        elif bch =='0':
                exb()
        else:
                print '[!] Fill in correctly'
                action()       
        raw_input('\n[Press Enter To Go Back]')
        os.system("python2 S.py")

if __name__ == '__main__':
        menu()
