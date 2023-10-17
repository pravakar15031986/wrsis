<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <footer>
         <p>Copyright <script>document.write(new Date().getFullYear());</script> Wheat Rust Surveillance Information System </p>
         <a href="#" id="scroll" style="display: none;"><span></span></a>
      </footer>
      <div class="setting-box">
         <div class="profile-details">
            <a class="setting-close" href="javascript:void(0)"><span class="icon-x1"></span></a>
            <div class="photo__section">
               
                <img src="viewProfilePhoto" alt="user" >
               <h4><c:out value="${sessionScope.FULLNAME}" /><small>(<c:out value="${sessionScope.DESIGNATION}" />)</small></h4>
            </div>
            <div class="change__list">
               <ul>
                  <li><a href="userProfile" data-toggle="tooltip" data-placement="top" data-original-title="Profile"><span class="icon-user1"></span></a></li>
                  <li><a href="changePassword" data-toggle="tooltip" data-placement="top" data-original-title="Change Password"><span class="icon-lock1"></span></a></li>
                  <li><a href="logout" data-toggle="tooltip" data-placement="top" data-original-title="Sign out"><span class="icon-log-out1"></span></a></li>
               </ul>
            </div>
         </div>
         <div class="theme-customizer">
            <h4>Menu option</h4>
            <ul class="menu-option-list">
               <li>Top menu <label class="switch " id="menu-dark">
                  <input type="checkbox" id="topmenucheck" class="default">
                  <span class="slider"></span>
                  </label>
               </li>
               <li>Dark <label class="switch " id="menu-dark">
                  <input type="checkbox" id="darkcheck" class="default">
                  <span class="slider"></span>
                  </label>
               </li>
            </ul>
            <h4>Theme Colors</h4>
            <ul class="theme-list">
               <li><a href="javascript:void(0)" id="custom" class="theme-blue"></a></li>
               <li><a href="javascript:void(0)" id="theme-sky" class="theme-sky-blue"></a></li>
               <li><a href="javascript:void(0)" id="theme-white" class="theme-white"></a></li>
               <li><a href="javascript:void(0)" id="theme-purple" class="theme-purple"></a></li>
               <li><a href="javascript:void(0)" id="theme-pink" class="theme-pink"></a></li>
               <li><a href="javascript:void(0)" id="theme-orange" class="theme-orange"></a></li>
               <li><a href="javascript:void(0)" id="theme-teal" class="theme-teal"></a></li>
               <li><a href="javascript:void(0)" id="theme-gray" class="theme-gray"></a></li>
            </ul>
         </div>
      </div>
      

<script>



	$(function(){

		$('.sidemenu .navbar-nav > li').on('mouseenter', function () {
            if ($(this).parents('.page-container').hasClass('display-full')) {
                var topVal = $(this).offset().top;
                if (topVal >= $(window).height() - 200) {
                    $('.navbar-nav .dropdown-menu').css({ 'top': 'auto', 'bottom': '10px' });
                }
                else {
                    $('.navbar-nav .dropdown-menu').css({ 'top': topVal + 'px', 'bottom': 'auto' });
                }
            }
        });
        $('.sidemenu .navbar-nav > li').on('mouseleave', function () {
            $('.navbar-nav .dropdown-menu').css({ 'top': 'auto', 'bottom': 'auto' });
        });
	
      
        
        
		var nav = function () {
                $('.side-nav > li > a').click(function () {

                    if ($('.side-nav > li').length == $(this).parents('li').index() + 1) {
                        $('.scroll-box').animate({ scrollTop: $('.side-nav-list').height() }, 100);
                    }
                    var gw_nav = $('.side-nav');
                    gw_nav.find('li').removeClass('active');
                    $('.side-nav > li > ul > li').removeClass('active');

                    var checkElement = $(this).parent();
                    var ulDom = checkElement.find('.submenu')[0];

                    //                    if (ulDom == undefined) {
                    //                        checkElement.addClass('active');
                    //                        $('.side-nav').find('li').find('ul:visible').slideToggle();
                    //                        return;
                    //                    }
                    // if (ulDom.style.display != 'block') {
                    if ($(this).hasClass('tglShow') == false) {
                        gw_nav.find('li').find('ul:visible').slideToggle();
                        gw_nav.find('li.init-arrow-up').removeClass('init-arrow-up').addClass('arrow-down');
                        gw_nav.find('li.arrow-up').removeClass('arrow-up').addClass('arrow-down');
                        checkElement.removeClass('init-arrow-down');
                        checkElement.removeClass('arrow-down');
                        checkElement.addClass('arrow-up');
                        checkElement.addClass('active');
                        checkElement.find('ul').slideDown(400);
                        // checkElement.find('ul').slideToggle(300);
                        $(this).addClass("tglShow")
                    }

                    else {
                        checkElement.removeClass('init-arrow-up');
                        checkElement.removeClass('arrow-up');
                        checkElement.removeClass('active');
                        checkElement.addClass('arrow-down');
                        checkElement.find('ul').slideUp(400);
                        // checkElement.find('ul').slideToggle(300);
                        $(this).removeClass("tglShow")

                    }
                });

            });

});

</script>

