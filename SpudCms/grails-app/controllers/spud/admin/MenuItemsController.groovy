package spud.admin
import  spud.cms.*
import  spud.core.*

@SpudApp(name="Menus", thumbnail="spud/admin/menus_thumb.png")
@SpudSecure(['MENUS'])
class MenuItemsController {
	static namespace = "spud_admin"

	def index() {
		def menu = loadMenu()
		if(!menu) {
			return
		}
		def menuItems = SpudMenuItem.findAllByMenu(menu)
		render view: '/spud/admin/menu_items/index', model:[menuItems: menuItems]
	}

	def create() {
		def menu = new SpudMenu()
  	render view: '/spud/admin/menus/create', model:[menu: menu]
	}

	def save() {
    if(!params.menu) {
      flash.error = "Menu submission not specified"
      redirect resource: 'menus', action: 'index', namespace: 'spud_admin'
      return
    }

    def menu = new SpudMenu(params.menu)



    if(menu.save(flush:true)) {
      redirect resource: 'menus', action: 'index', namespace: 'spud_admin'
    } else {
      flash.error = "Error Saving Menu"
      render view: '/spud/admin/menus/create', model:[menu:menu]
    }
	}

	def edit = {
		def menu = loadMenu()
		if(!menu) {
			return
		}
    render view: '/spud/admin/menus/edit', model: [menu: menu]

	}

	def update() {
		def menu = loadMenu()
		if(!menu) {
			return
		}
    menu.properties += params.menu

    if(menu.save(flush:true)) {
      redirect resource: 'menus', action: 'index', namespace: 'spud_admin'
    } else {
      render view: '/spud/admin/menus/edit', model: [menu: menu]
    }
	}

	def delete() {
		def menu = loadMenu()
		if(!menu) {
			return
		}
		menu.delete()
		flash.notice = "Menu Removed Successfully!"
    redirect resource: 'menus', action: 'index', namespace: 'spud_admin'
	}

	private loadMenu() {
  	if(!params.menusId) {
			flash.error = "Menu Id Not Specified For Listing Menu Items"
			redirect controller: 'menus', action: 'index', namespace: 'spud_admin'
			return null
		}

		def menu = SpudMenu.get(params.menusId)
		if(!menu) {
			flash.error = "Menu not found for listing Menu Items!"
			redirect controller: 'menus', action: 'index', namespace: 'spud_admin'
			return null
		}
		return menu
	}
}